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
        enrollmentPlanFieldMap.put("schoolId", EnrollmentPlan::getSchoolId);
        enrollmentPlanFieldMap.put("schoolName", EnrollmentPlan::getSchoolName);
        enrollmentPlanFieldMap.put("city", EnrollmentPlan::getCity);
        enrollmentPlanFieldMap.put("year", EnrollmentPlan::getYear);
        enrollmentPlanFieldMap.put("wenli", EnrollmentPlan::getWenli);
        enrollmentPlanFieldMap.put("pici", EnrollmentPlan::getPici);
        enrollmentPlanFieldMap.put("subjectName", EnrollmentPlan::getSubjectName);
        enrollmentPlanFieldMap.put("enrollmentNumber", EnrollmentPlan::getEnrollmentNumber);
        enrollmentPlanFieldMap.put("studyYear", EnrollmentPlan::getStudyYear);
        enrollmentPlanFieldMap.put("tuition", EnrollmentPlan::getTuition);
        enrollmentPlanFieldMap.put("subjectRequirements", EnrollmentPlan::getSubjectRequirements);

        schoolInfoMap.put("schoolId", SchoolInfo::getSchoolId);
        schoolInfoMap.put("schoolName", SchoolInfo::getSchoolName);
        schoolInfoMap.put("tags", SchoolInfo::getTags);
        schoolInfoMap.put("address", SchoolInfo::getAddress);
        schoolInfoMap.put("website", SchoolInfo::getWebsite);
        schoolInfoMap.put("phone", SchoolInfo::getPhone);
        schoolInfoMap.put("shisu", SchoolInfo::getShisu);
        schoolInfoMap.put("detail", SchoolInfo::getDetail);

        schoolScoreMap.put("schoolId", SchoolScore::getSchoolId);
        schoolScoreMap.put("schoolName", SchoolScore::getSchoolName);
        schoolScoreMap.put("city", SchoolScore::getCity);
        schoolScoreMap.put("year", SchoolScore::getYear);
        schoolScoreMap.put("wenli", SchoolScore::getWenli);
        schoolScoreMap.put("admissionBatch", SchoolScore::getAdmissionBatch);
        schoolScoreMap.put("enrollmentType", SchoolScore::getEnrollmentType);
        schoolScoreMap.put("minScorePosition", SchoolScore::getMinScorePosition);
        schoolScoreMap.put("provinceControlLine", SchoolScore::getProvinceControlLine);
        schoolScoreMap.put("majorGroup", SchoolScore::getMajorGroup);
        schoolScoreMap.put("subjectRequirements", SchoolScore::getSubjectRequirements);

        subjectScoreMap.put("schoolId", SubjectScore::getSchoolId);
        subjectScoreMap.put("schoolName", SubjectScore::getSchoolName);
        subjectScoreMap.put("city", SubjectScore::getCity);
        subjectScoreMap.put("year", SubjectScore::getYear);
        subjectScoreMap.put("wenli", SubjectScore::getWenli);
        subjectScoreMap.put("pici", SubjectScore::getPici);
        subjectScoreMap.put("subjectName", SubjectScore::getSubjectName);
        subjectScoreMap.put("admissionBatch", SubjectScore::getAdmissionBatch);
        subjectScoreMap.put("minScorePosition", SubjectScore::getMinScorePosition);
        subjectScoreMap.put("subjectRequirements", SubjectScore::getSubjectRequirements);
    }

    public static <T> List<T> executeQuery(BaseMapper<T> mapper, List<String> conditions, List<String> fields, Supplier<Map<String, SFunction<T, ?>>> fieldMapSupplier) {
        if (CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty((conditions))) {
            throw new RuntimeException("查询条件不能为空");
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
