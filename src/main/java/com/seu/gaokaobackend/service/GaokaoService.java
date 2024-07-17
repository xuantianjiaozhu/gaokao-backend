package com.seu.gaokaobackend.service;

import com.seu.gaokaobackend.model.dto.QueryParam;
import com.seu.gaokaobackend.model.dto.QueryResult;

import java.util.List;

public interface GaokaoService {

    String chatProcess(String prompt);

    QueryResult getInfoForLLM(List<QueryParam> queryParamList);
}
