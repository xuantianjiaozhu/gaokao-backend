package com.seu.gaokaobackend.model.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JSONField(name = "school_id")
    @JsonProperty("school_id")
    private Integer schoolId;

    /**
     * 学校名称
     */
    @TableField(value = "school_name")
    @JSONField(name = "school_name")
    @JsonProperty("school_name")
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
    @TableField(value = "pici")
    private String pici;

    /**
     * 招生类型
     */
    @TableField(value = "enrollment_type")
    @JSONField(name = "enrollment_type")
    @JsonProperty("enrollment_type")
    private String enrollmentType;

    /**
     * 最低分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 最低位次
     */
    @TableField(value = "position")
    private Integer position;

    /**
     * 省控线
     */
    @TableField(value = "province_control_line")
    @JSONField(name = "province_control_line")
    @JsonProperty("province_control_line")
    private String provinceControlLine;

    /**
     * 专业组
     */
    @TableField(value = "major_group")
    @JSONField(name = "major_group")
    @JsonProperty("major_group")
    private String majorGroup;

    /**
     * 选科要求
     */
    @TableField(value = "subject_requirements")
    @JSONField(name = "subject_requirements")
    @JsonProperty("subject_requirements")
    private String subjectRequirements;
}