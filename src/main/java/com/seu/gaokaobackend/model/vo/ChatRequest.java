package com.seu.gaokaobackend.model.vo;

import com.seu.gaokaobackend.model.dto.LlmMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {

    private String uuid;

    private List<LlmMessage> messages;

    private String province;

    private String wenli;

    private Integer score;

    private Integer rank;
}
