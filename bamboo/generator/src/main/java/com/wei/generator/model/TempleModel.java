package com.wei.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempleModel {

    private String fileName;
    private String packageName;
    private List<String> importName;

    private List<XmlColumnModel> columnsInfo;

    private String dir;
    private String projectPath;
    private String htmlModelName;

}
