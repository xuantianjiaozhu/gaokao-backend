package com.seu.gaokaobackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName enrollment_plan
 */
@TableName(value ="enrollment_plan")
@Data
public class EnrollmentPlan implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学校的编号（url 里的）
     */
    @TableField(value = "school_id")
    private Integer schoolId;

    /**
     * 学校名称
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 年份
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 选科
     */
    @TableField(value = "wenli")
    private String wenli;

    /**
     * 批次
     */
    @TableField(value = "pici")
    private String pici;

    /**
     * 专业名称
     */
    @TableField(value = "subject_name")
    private String subjectName;

    /**
     * 计划招生
     */
    @TableField(value = "enrollment_number")
    private String enrollmentNumber;

    /**
     * 学制
     */
    @TableField(value = "study_year")
    private String studyYear;

    /**
     * 学费
     */
    @TableField(value = "tuition")
    private String tuition;

    /**
     * 选科要求
     */
    @TableField(value = "subject_requirements")
    private String subjectRequirements;
}