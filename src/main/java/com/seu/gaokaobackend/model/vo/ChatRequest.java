package com.seu.gaokaobackend.model.vo;

import com.seu.gaokaobackend.model.dto.LlmMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {

    private String uuid;

    private String prompt;

    private List<LlmMessage> historyMessages;

    private String province;

    private String wenli;

    private Integer score;

    private Integer rank;
}
