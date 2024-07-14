package com.szw.gaokaobackend.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szw.gaokaobackend.model.QueryParam;
import com.szw.gaokaobackend.model.QueryResult;
import com.szw.gaokaobackend.service.*;
import com.szw.gaokaobackend.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
