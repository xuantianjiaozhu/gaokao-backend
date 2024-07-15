package com.seu.gaokaobackend.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryParam {

    private String model;
    private List<String> conditions;
    private List<String> fields;
}
