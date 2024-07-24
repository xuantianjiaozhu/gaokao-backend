package com.seu.gaokaobackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.seu.gaokaobackend.model.dto.*;
import com.seu.gaokaobackend.model.vo.ChatResponse;
import com.seu.gaokaobackend.service.*;
import com.seu.gaokaobackend.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GaokaoServiceImpl implements GaokaoService {

    @Autowired
    private EnrollmentPlanService enrollmentPlanService;
    @Autowired
    private SchoolInfoService schoolInfoService;
    @Autowired
    private SchoolScoreService schoolScoreService;
    @Autowired
    private SubjectScoreService subjectScoreService;

    @Value("${llm.path}")
    private String llmPath;

    @Value("${llm.model}")
    private String llmModel;

    @Autowired
    private WebClient webClient;

    private static final String USER = "user";
    private static final String ASSISTANT = "assistant";

    private static String promptTemplateFirst;
    private static String promptTemplateSecond;

    static {
        ClassPathResource promptFirstResource = new ClassPathResource("prompt_template_first.md");
        ClassPathResource promptSecondResource = new ClassPathResource("prompt_template_second.md");
        try {
            promptTemplateFirst = promptFirstResource.getContentAsString(StandardCharsets.UTF_8);
            promptTemplateSecond = promptSecondResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("File not exist", e);
        }
    }

    @Override
    public Flux<ChatResponse> chatProcess(String prompt, List<LlmMessage> historyMessages,
                                          String province, String wenli, Integer score, Integer rank) {
        LlmRequest firstLlmRequest = adjustLlmRequest(historyMessages, promptTemplateFirst, prompt,
                province, wenli, score, rank);
        log.info("first llm request is: {}", JSON.toJSONString(firstLlmRequest));
        return webClient.post()
                .uri(llmPath)
                .bodyValue(firstLlmRequest)
                .retrieve()
                .bodyToFlux(String.class)
                .switchOnFirst((signal, flux) -> {
                    String first = signal.get();
                    Assert.notNull(first, "first is null");
                    String firstContent = getContent(first);
                    if (needSql(firstContent)) {
                        StringBuilder sb = new StringBuilder();
                        return flux.collectList().flatMapMany(list -> {
                            list.forEach(e -> sb.append(getContent(e)));
                            String jsonString = removeMarkdownCodeBlock(sb.toString(), "json");
                            List<QueryParam> queryParamList = JSON.parseArray(jsonString, QueryParam.class);
                            log.info("query param list is: {}", JSON.toJSONString(queryParamList));
                            QueryResult queryResult = getInfoForLLM(queryParamList);
                            String queryResultJson = JSON.toJSONString(queryResult);
                            String promptTemplateSecondWithSqlResult = promptTemplateSecond
                                    .replace("\n\n\n\n", String.format("\n\n%s\n\n", queryResultJson));
                            LlmRequest secondLlmRequest = adjustLlmRequest(historyMessages,
                                    promptTemplateSecondWithSqlResult, prompt,
                                    province, wenli, score, rank);
                            log.info("second llm request is: {}", JSON.toJSONString(secondLlmRequest));
                            return webClient.post()
                                    .uri(llmPath)
                                    .bodyValue(secondLlmRequest)
                                    .retrieve()
                                    .bodyToFlux(String.class)
                                    .map(GaokaoServiceImpl::getChatResponse);
                        });
                    } else {
                        return flux.map(GaokaoServiceImpl::getChatResponse);
                    }
                })
                .doOnError(e -> log.error("chat process error: {}", e.getMessage(), e))
                .onErrorReturn(new ChatResponse(false, "服务器错误，请重试。"));
    }

    @Override
    public QueryResult getInfoForLLM(List<QueryParam> queryParamList) {
        QueryResult result = new QueryResult();
        for (QueryParam queryParam : queryParamList) {
            switch (queryParam.getModel()) {
                case "school_info": {
                    result.setSchoolInfoList(
                            QueryUtils.executeQuery(schoolInfoService.getBaseMapper(),
                                    queryParam.getConditions(),
                                    queryParam.getFields(),
                                    () -> QueryUtils.schoolInfoMap)
                    );
                    break;
                }
                case "school_score": {
                    result.setSchoolScoreList(
                            QueryUtils.executeQuery(schoolScoreService.getBaseMapper(),
                                    queryParam.getConditions(),
                                    queryParam.getFields(),
                                    () -> QueryUtils.schoolScoreMap)
                    );
                    break;
                }
                case "subject_score": {
                    result.setSubjectScoreList(
                            QueryUtils.executeQuery(subjectScoreService.getBaseMapper(),
                                    queryParam.getConditions(),
                                    queryParam.getFields(),
                                    () -> QueryUtils.subjectScoreMap)
                    );
                    break;
                }
                case "enrollment_plan": {
                    result.setEnrollmentPlanList(
                            QueryUtils.executeQuery(enrollmentPlanService.getBaseMapper(),
                                    queryParam.getConditions(),
                                    queryParam.getFields(),
                                    () -> QueryUtils.enrollmentPlanFieldMap)
                    );
                    break;
                }
                default: {
                    throw new RuntimeException("Invalid model name");
                }
            }
        }
        return result;
    }

    private LlmRequest adjustLlmRequest(List<LlmMessage> historyMessages, String promptTemplate,
                                        String prompt, String province, String wenli, Integer score, Integer rank) {
        LlmRequest res = new LlmRequest();
        res.setModel(llmModel);
        LlmMessage lastMessage = new LlmMessage();
        lastMessage.setRole(USER);
        String promptWithTemplate = promptTemplate + prompt;
        promptWithTemplate = promptWithTemplate.replace("`${province}`", province)
                .replace("`${wenli}`", wenli)
                .replace("`${score}`", score.toString())
                .replace("`${rank}`", rank.toString());
        lastMessage.setContent(promptWithTemplate);
        List<LlmMessage> historyMessagesCopy = new ArrayList<>(historyMessages);
        historyMessagesCopy.add(lastMessage);
        res.setMessages(historyMessagesCopy);
        return res;
    }

    private static boolean needSql(String response) {
        return response.charAt(0) == '[' || response.charAt(0) == '`';
    }

    private static String getContent(String line) {
        return JSON.parseObject(line.trim(), LlmResponse.class).getMessage().getContent();
    }

    private static ChatResponse getChatResponse(String line) {
        LlmResponse res = JSON.parseObject(line.trim(), LlmResponse.class);
        return new ChatResponse(res.getDone(), res.getMessage().getContent());
    }

    private static String removeMarkdownCodeBlock(String markdown, String language) {
        // 去掉开头的 ```json 和结尾的 ```
        return markdown.replaceAll("(?s)```" + language + "\\n(.*?)\\n```", "$1").trim();
    }
}
