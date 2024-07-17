package com.seu.gaokaobackend.model.dto;

import com.seu.gaokaobackend.model.entity.EnrollmentPlan;
import com.seu.gaokaobackend.model.entity.SchoolInfo;
import com.seu.gaokaobackend.model.entity.SchoolScore;
import com.seu.gaokaobackend.model.entity.SubjectScore;
import lombok.Data;

import java.util.List;

@Data
public class QueryResult {

    private List<EnrollmentPlan> enrollmentPlanList;
    private List<SchoolInfo> schoolInfoList;
    private List<SchoolScore> schoolScoreList;
    private List<SubjectScore> subjectScoreList;
}
