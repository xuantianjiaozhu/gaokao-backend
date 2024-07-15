package com.seu.gaokaobackend.controller;

import com.seu.gaokaobackend.model.ChatRequest;
import com.seu.gaokaobackend.service.GaokaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class GaokaoController {

    @Autowired
    private GaokaoService gaokaoService;

    @PostMapping("/chat-process")
    public String chatProcess(@RequestBody ChatRequest req) {
        return gaokaoService.chatProcess(req.getPrompt());
    }
}
