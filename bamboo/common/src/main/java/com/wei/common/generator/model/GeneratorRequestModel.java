package com.wei.common.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorRequestModel {

    private String projectModule;
    private String basePackage;
    private String module;
    private String author;

    private List<String> tableNames;

}
