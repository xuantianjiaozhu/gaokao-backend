package com.seu.gaokaobackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName subject_score
 */
@TableName(value ="subject_score")
@Data
public class SubjectScore implements Serializable {
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
     * 专业名称
     */
    @TableField(value = "subject_name")
    private String subjectName;

    /**
     * 录取批次
     */
    @TableField(value = "pici")
    private String pici;

    /**
     * 最低分/最低位次
     */
    @TableField(value = "min_score_position")
    private String minScorePosition;

    /**
     * 选科要求
     */
    @TableField(value = "subject_requirements")
    private String subjectRequirements;
}