package com.szw.gaokaobackend.model;

import lombok.Data;

@Data
public class ChatRequest {
    /**
     * 客户端发送的问题参数
     */
    private String prompt;
}
