package com.seu.gaokaobackend.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult {

    private List<EnrollmentPlan> enrollmentPlanList;
    private List<SchoolInfo> schoolInfoList;
    private List<SchoolScore> schoolScoreList;
    private List<SubjectScore> subjectScoreList;
}
