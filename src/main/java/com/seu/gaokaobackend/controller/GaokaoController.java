package com.seu.gaokaobackend.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.seu.gaokaobackend.model.dto.LlmMessage;
import com.seu.gaokaobackend.model.exception.BusinessException;
import com.seu.gaokaobackend.model.vo.ChatRequest;
import com.seu.gaokaobackend.model.vo.ChatResponse;
import com.seu.gaokaobackend.model.vo.Result;
import com.seu.gaokaobackend.service.GaokaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping
@Slf4j
public class GaokaoController {

    @Autowired
    private GaokaoService gaokaoService;

    private static final ConcurrentHashMap<String, ChatRequest> SESSION_MAP = new ConcurrentHashMap<>();

    @PostMapping("/start-chat-session")
    public Result<Void> startChatSession(@RequestBody ChatRequest req) {
        if (ObjectUtils.isNull(req.getProvince(), req.getWenli(), req.getScore(), req.getRank())) {
            throw new BusinessException("同学你好，请先点击左下角设置按钮输入你的省份、文理选科、高考成绩、位次。");
        }
        SESSION_MAP.put(req.getUuid(), req);
        return Result.success();
    }

    @GetMapping(value = "/chat-process", produces = "text/event-stream")
    public Flux<ChatResponse> chatProcess(@RequestParam String uuid) {
        ChatRequest req = SESSION_MAP.get(uuid);
        String prompt = req.getPrompt();
        List<LlmMessage> historyMessages = req.getHistoryMessages();
        Assert.notNull(prompt, "prompt is null");
        SESSION_MAP.remove(uuid);
        return gaokaoService.chatProcess(prompt, historyMessages, req.getProvince(), req.getWenli(), req.getScore(), req.getRank());
    }
}
