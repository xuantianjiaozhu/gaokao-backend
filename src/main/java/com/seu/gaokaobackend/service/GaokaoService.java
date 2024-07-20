package com.seu.gaokaobackend.service;

import com.seu.gaokaobackend.model.dto.LlmMessage;
import com.seu.gaokaobackend.model.dto.QueryParam;
import com.seu.gaokaobackend.model.dto.QueryResult;
import com.seu.gaokaobackend.model.vo.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface GaokaoService {

    Flux<ChatResponse> chatProcess(List<LlmMessage> messages, String province, String wenli, Integer score, Integer rank);

    QueryResult getInfoForLLM(List<QueryParam> queryParamList);
}
