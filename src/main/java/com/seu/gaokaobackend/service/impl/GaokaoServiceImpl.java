package com.seu.gaokaobackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.seu.gaokaobackend.model.dto.*;
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
        ClassPathResource promptFirstResource = new ClassPathResource("prompt_template_first.txt");
        ClassPathResource promptSecondResource = new ClassPathResource("prompt_template_second.txt");
        try {
            promptTemplateFirst = promptFirstResource.getContentAsString(StandardCharsets.UTF_8);
            promptTemplateSecond = promptSecondResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("File not exist", e);
        }
    }

    @Override
    public Flux<String> chatProcess(String prompt) {
        String promptWithTemplateFirst = promptTemplateFirst + prompt;
        LlmRequest firstLlmRequest = adjustLlmRequest(promptWithTemplateFirst);
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
                        return flux.collectList().flatMapMany(list -> {
                            List<QueryParam> queryParamList = JSON.parseArray(JSON.toJSONString(list), QueryParam.class);
                            QueryResult queryResult = getInfoForLLM(queryParamList);
                            String queryResultJson = JSON.toJSONString(queryResult);
                            String promptWithTemplateSecond = promptTemplateSecond
                                    .replace("\n\n\n\n", String.format("\n\n%s\n\n", queryResultJson)) + prompt;
                            LlmRequest secondLlmRequest = adjustLlmRequest(promptWithTemplateSecond);
                            return webClient.post()
                                    .uri(llmPath)
                                    .bodyValue(secondLlmRequest)
                                    .retrieve()
                                    .bodyToFlux(String.class)
                                    .map(GaokaoServiceImpl::getContent);
                        });
                    } else {
                        return flux.map(GaokaoServiceImpl::getContent);
                    }
                });
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

    private LlmRequest adjustLlmRequest(String prompt) {
        LlmRequest res = new LlmRequest();
        res.setModel(llmModel);
        res.setMessages(List.of(new LlmMessage(USER, prompt)));
        return res;
    }

    private static boolean needSql(String response) {
        return response.charAt(0) == '[';
    }

    private static String getContent(String line) {
        return JSON.parseObject(line.trim(), LlmResponse.class).getMessage().getContent();
    }
}
