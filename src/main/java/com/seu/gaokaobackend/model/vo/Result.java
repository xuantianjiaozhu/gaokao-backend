package com.seu.gaokaobackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 统一返回数据结构
 *
 * @param <T>
 * @author sheh
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Result<T> {

    private Integer status;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return new Result<>(ResultEnum.SUCCESS.getStatus(), ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS.getStatus(), ResultEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultEnum.FAIL.getStatus(), ResultEnum.FAIL.getMessage(), null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultEnum.FAIL.getStatus(), message, null);
    }

    public static <T> Result<T> fail(IResult errorResult) {
        return new Result<>(errorResult.getStatus(), errorResult.getMessage(), null);
    }

    public static <T> Result<T> instance(Integer status, String message, T data) {
        Result<T> result = new Result<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
