package com.wei.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnModel {

    private String name;
    private boolean isId = false;
    private boolean isNotEmpty = false;
    private boolean isNotNull = false;
    private String javaType;
    private String jdbcType;
    private String columnLength;
    private boolean isMultiLanguage = false;
    private String remarks;

}
