package com.seu.gaokaobackend.controller;

import com.seu.gaokaobackend.model.vo.ChatRequest;
import com.seu.gaokaobackend.model.vo.Result;
import com.seu.gaokaobackend.service.GaokaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping
@Slf4j
public class GaokaoController {

    @Autowired
    private GaokaoService gaokaoService;

    private ConcurrentMap<String, SseEmitter> sseEmitterMap;

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(@RequestParam String uuid) {
        SseEmitter sse = new SseEmitter(30 * 1000L);
        sse.onCompletion(() -> {
            log.info("onCompletion, uuid:{}", uuid);
            sseEmitterMap.remove(uuid);
        });
        sse.onTimeout(() -> {
            log.info("onTimeout, uuid:{}", uuid);
            sseEmitterMap.remove(uuid);
        });
        sse.onError((e) -> {
            log.error("onError, uuid:{}", uuid, e);
            sseEmitterMap.remove(uuid);
        });
        sseEmitterMap.put(uuid, sse);
        log.info("initialize sse, uuid:{}", uuid);
        return sse;
    }

    @GetMapping("/close-sse")
    public Result<Void> closeSse(@RequestParam String uuid) {
        log.info("close sse, uuid:{}", uuid);
        if (Objects.nonNull(sseEmitterMap.get(uuid))) {
            sseEmitterMap.get(uuid).complete();
        }
        return Result.success();
    }

    @PostMapping("/chat-process")
    public String chatProcess(@RequestBody ChatRequest req) {
        return gaokaoService.chatProcess(req.getPrompt());
    }
}
