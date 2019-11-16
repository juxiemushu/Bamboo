package com.wei.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XmlColumnModel {

    private String tableColumnsName;
    private String dBColumnsName;
    private String jdbcType;

}
