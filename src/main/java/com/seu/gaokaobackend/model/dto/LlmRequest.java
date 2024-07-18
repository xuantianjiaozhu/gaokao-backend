package com.seu.gaokaobackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class LlmRequest {

    private String model;

    private List<LlmMessage> messages;
}
