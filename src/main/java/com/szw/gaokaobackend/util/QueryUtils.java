package com.szw.gaokaobackend.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.szw.gaokaobackend.model.EnrollmentPlan;
import com.szw.gaokaobackend.model.SchoolInfo;
import com.szw.gaokaobackend.model.SchoolScore;
import com.szw.gaokaobackend.model.SubjectScore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QueryUtils {
    public static final Map<String, SFunction<EnrollmentPlan, ?>> enrollmentPlanFieldMap = new HashMap<>();
    public static final Map<String, SFunction<SchoolInfo, ?>> schoolInfoMap = new HashMap<>();
    public static final Map<String, SFunction<SchoolScore, ?> > schoolScoreMap = new HashMap<>();
    public static final Map<String, SFunction<SubjectScore, ?>> subjectScoreMap = new HashMap<>();

    static {
        enrollmentPlanFieldMap.put("school_id", EnrollmentPlan::getSchoolId);
        enrollmentPlanFieldMap.put("school_name", EnrollmentPlan::getSchoolName);
        enrollmentPlanFieldMap.put("city", EnrollmentPlan::getCity);
        enrollmentPlanFieldMap.put("year", EnrollmentPlan::getYear);
        enrollmentPlanFieldMap.put("wenli", EnrollmentPlan::getWenli);
        enrollmentPlanFieldMap.put("pici", EnrollmentPlan::getPici);
        enrollmentPlanFieldMap.put("subject_name", EnrollmentPlan::getSubjectName);
        enrollmentPlanFieldMap.put("enrollment_number", EnrollmentPlan::getEnrollmentNumber);
        enrollmentPlanFieldMap.put("study_year", EnrollmentPlan::getStudyYear);
        enrollmentPlanFieldMap.put("tuition", EnrollmentPlan::getTuition);
        enrollmentPlanFieldMap.put("subject_requirements", EnrollmentPlan::getSubjectRequirements);

        schoolInfoMap.put("schoolId", SchoolInfo::getSchoolId);
        schoolInfoMap.put("school_name", SchoolInfo::getSchoolName);
        schoolInfoMap.put("tags", SchoolInfo::getTags);
        schoolInfoMap.put("address", SchoolInfo::getAddress);
        schoolInfoMap.put("website", SchoolInfo::getWebsite);
        schoolInfoMap.put("phone", SchoolInfo::getPhone);
        schoolInfoMap.put("shisu", SchoolInfo::getShisu);
        schoolInfoMap.put("detail", SchoolInfo::getDetail);

        schoolScoreMap.put("school_id", SchoolScore::getSchoolId);
        schoolScoreMap.put("school_name", SchoolScore::getSchoolName);
        schoolScoreMap.put("city", SchoolScore::getCity);
        schoolScoreMap.put("year", SchoolScore::getYear);
        schoolScoreMap.put("wenli", SchoolScore::getWenli);
        schoolScoreMap.put("admission_batch", SchoolScore::getAdmissionBatch);
        schoolScoreMap.put("enrollment_type", SchoolScore::getEnrollmentType);
        schoolScoreMap.put("min_score_position", SchoolScore::getMinScorePosition);
        schoolScoreMap.put("province_control_line", SchoolScore::getProvinceControlLine);
        schoolScoreMap.put("major_group", SchoolScore::getMajorGroup);
        schoolScoreMap.put("subject_requirements", SchoolScore::getSubjectRequirements);

        subjectScoreMap.put("school_id", SubjectScore::getSchoolId);
        subjectScoreMap.put("school_name", SubjectScore::getSchoolName);
        subjectScoreMap.put("city", SubjectScore::getCity);
        subjectScoreMap.put("year", SubjectScore::getYear);
        subjectScoreMap.put("wenli", SubjectScore::getWenli);
        subjectScoreMap.put("pici", SubjectScore::getPici);
        subjectScoreMap.put("subject_name", SubjectScore::getSubjectName);
        subjectScoreMap.put("admission_batch", SubjectScore::getAdmissionBatch);
        subjectScoreMap.put("min_score_position", SubjectScore::getMinScorePosition);
        subjectScoreMap.put("subject_requirements", SubjectScore::getSubjectRequirements);
    }

    public static <T> List<T> executeQuery(BaseMapper<T> mapper, List<String> conditions, List<String> fields, Supplier<Map<String, SFunction<T, ?>>> fieldMapSupplier) {
        if (CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty((conditions))) {
            throw new RuntimeException("QueryUtils: fields or conditions is empty");
        }
        // List<String> fields 转换为 SFunction<T, ?>[] fieldSelectors
        // 通过传递的 Supplier 获取对应的映射
        Map<String, SFunction<T, ?>> fieldMap = fieldMapSupplier.get();
        // 转换字段名到 SFunction
        SFunction<T, ?>[] fieldSelectors = fields.stream()
                .map(fieldMap::get)
                .toArray(SFunction[]::new);

        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(fieldSelectors);

        queryWrapper.select(fieldSelectors);
        for (String condition : conditions) {
            queryWrapper.apply(condition);
        }
        return mapper.selectList(queryWrapper);
    }
}
