package com.seu.gaokaobackend.controller;

import com.seu.gaokaobackend.model.dto.LlmMessage;
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
        SESSION_MAP.put(req.getUuid(), req);
        return Result.success();
    }

    @GetMapping(value = "/chat-process", produces = "text/event-stream")
    public Flux<ChatResponse> chatProcess(@RequestParam String uuid) {
        ChatRequest req = SESSION_MAP.get(uuid);
        List<LlmMessage> messages = req.getMessages();
        Assert.notNull(messages, "session not found");
        SESSION_MAP.remove(uuid);
        return gaokaoService.chatProcess(messages, req.getProvince(), req.getWenli(), req.getScore(), req.getRank());
    }
}
