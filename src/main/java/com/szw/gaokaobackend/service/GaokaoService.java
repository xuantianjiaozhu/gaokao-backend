package com.szw.gaokaobackend.service;

import com.szw.gaokaobackend.model.QueryParam;
import com.szw.gaokaobackend.model.QueryResult;

import java.util.List;

public interface GaokaoService {

    QueryResult getInfoForLLM(List<QueryParam> queryParamList);
}
