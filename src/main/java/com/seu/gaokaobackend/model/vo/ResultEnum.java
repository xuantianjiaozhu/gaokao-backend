package com.seu.gaokaobackend.model.vo;

import lombok.AllArgsConstructor;

/**
 * 常用结果的枚举
 *
 * @author sheh
 */
@AllArgsConstructor
public enum ResultEnum implements IResult {

    SUCCESS(200, "success"),
    FAIL(500, "fail");

    private final Integer status;

    private final String message;

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}