package com.seu.gaokaobackend.config;

import com.seu.gaokaobackend.model.exception.BusinessException;
import com.seu.gaokaobackend.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(value = "com.seu.gaokaobackend.controller")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<String> exception(BusinessException e) {
        log.error("Business Exception:", e);
        return Result.fail(e.getMessage());
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<String> exception(Exception e) {
        log.error("Global Exception: {}", e.getMessage(), e);
        return Result.fail("服务器错误，请重试。");
    }
}