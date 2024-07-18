package com.seu.gaokaobackend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LlmResponse {
    private String model;
    private LocalDateTime createdAt;
    private LlmMessage message;
    private String doneReason;
    private Boolean done;
}
