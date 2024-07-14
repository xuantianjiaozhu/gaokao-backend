package com.szw.gaokaobackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName school_score
 */
@TableName(value ="school_score")
@Data
public class SchoolScore implements Serializable {
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
     * 录取批次
     */
    @TableField(value = "admission_batch")
    private String admissionBatch;

    /**
     * 招生类型
     */
    @TableField(value = "enrollment_type")
    private String enrollmentType;

    /**
     * 最低分/最低位次
     */
    @TableField(value = "min_score_position")
    private String minScorePosition;

    /**
     * 省控线
     */
    @TableField(value = "province_control_line")
    private String provinceControlLine;

    /**
     * 专业组
     */
    @TableField(value = "major_group")
    private String majorGroup;

    /**
     * 选科要求
     */
    @TableField(value = "subject_requirements")
    private String subjectRequirements;
}