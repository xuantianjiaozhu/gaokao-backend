package com.seu.gaokaobackend.model.dto;

import lombok.Data;

@Data
public class LlmResponse {
    private String model;
    private LlmMessage message;
    private String doneReason;
    private Boolean done;
}
