package com.seu.gaokaobackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.seu.gaokaobackend.model.dto.QueryParam;
import com.seu.gaokaobackend.model.dto.QueryResult;
import com.seu.gaokaobackend.service.*;
import com.seu.gaokaobackend.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${llm.host}")
    private String llmHost;

    @Value("${llm.path}")
    private String llmPath;

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
    public String chatProcess(String prompt) {
        String promptWithTemplateFirst = promptTemplateFirst + prompt;
        String llmRequestUrl = llmHost + llmPath;
        String llmResponse = restTemplate.postForObject(llmRequestUrl, promptWithTemplateFirst, String.class);
        if (needSql(llmResponse)) {
            List<QueryParam> queryParamList = JSON.parseArray(llmResponse, QueryParam.class);
            QueryResult queryResult = getInfoForLLM(queryParamList);
            String queryResultJson = JSON.toJSONString(queryResult);
            String promptWithTemplateSecond = promptTemplateSecond.replace("\n\n\n\n", String.format("\n\n%s\n\n", queryResultJson)) + prompt;
            return restTemplate.postForObject(llmRequestUrl, promptWithTemplateSecond, String.class);
        } else {
            return llmResponse;
        }
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

    private static boolean needSql(String response) {
        return response.charAt(0) == '[';
    }
}
