package com.seu.gaokaobackend.controller;

import com.seu.gaokaobackend.model.vo.ChatRequest;
import com.seu.gaokaobackend.service.GaokaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
@Slf4j
public class GaokaoController {

    @Autowired
    private GaokaoService gaokaoService;

    @PostMapping(value = "/chat-process", produces = "text/event-stream")
    public Flux<String> chatProcess(@RequestBody ChatRequest req) {
        return gaokaoService.chatProcess(req.getPrompt());
    }
}
