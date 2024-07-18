package com.seu.gaokaobackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LlmMessage {

    private String role;

    private String content;
}
