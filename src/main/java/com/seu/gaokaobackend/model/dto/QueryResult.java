package com.seu.gaokaobackend.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seu.gaokaobackend.model.entity.EnrollmentPlan;
import com.seu.gaokaobackend.model.entity.SchoolInfo;
import com.seu.gaokaobackend.model.entity.SchoolScore;
import com.seu.gaokaobackend.model.entity.SubjectScore;
import lombok.Data;

import java.util.List;

@Data
public class QueryResult {

    @JsonProperty("enrollment_plan_list")
    @JSONField(name = "enrollment_plan_list")
    private List<EnrollmentPlan> enrollmentPlanList;
    @JsonProperty("school_info_list")
    @JSONField(name = "school_info_list")
    private List<SchoolInfo> schoolInfoList;
    @JsonProperty("school_score_list")
    @JSONField(name = "school_score_list")
    private List<SchoolScore> schoolScoreList;
    @JsonProperty("subject_score_list")
    @JSONField(name = "subject_score_list")
    private List<SubjectScore> subjectScoreList;
}
