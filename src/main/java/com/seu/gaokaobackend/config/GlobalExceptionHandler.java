package com.seu.gaokaobackend.config;

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

    /**
     * token失效异常
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public Result<String> exception(Exception e) {
        log.error("Global Exception:", e);
        return Result.fail("服务器错误，请重试。");
    }

}