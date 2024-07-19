package com.seu.gaokaobackend.model.vo;

import lombok.Data;

@Data
public class ChatRequest {

    private String uuid;
    /**
     * 客户端发送的问题参数
     */
    private String prompt;
}
