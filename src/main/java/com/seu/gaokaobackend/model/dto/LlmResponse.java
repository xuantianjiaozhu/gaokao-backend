package com.seu.gaokaobackend.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LlmResponse {
    private String model;
    private LlmMessage message;
    private String doneReason;
    private Boolean done;
}
