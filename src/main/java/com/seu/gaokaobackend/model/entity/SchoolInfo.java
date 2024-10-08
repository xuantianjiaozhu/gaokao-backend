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
 * @TableName school_info
 */
@TableName(value ="school_info")
@Data
public class SchoolInfo implements Serializable {
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
     * 本科, 985, 双一流
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 学校省市
     */
    @TableField(value = "address")
    private String address;

    /**
     * 学校网址
     */
    @TableField(value = "website")
    private String website;

    /**
     * 学校电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 学校食宿
     */
    @TableField(value = "shisu")
    private String shisu;

    /**
     * 学校详情
     */
    @TableField(value = "detail")
    private String detail;
}