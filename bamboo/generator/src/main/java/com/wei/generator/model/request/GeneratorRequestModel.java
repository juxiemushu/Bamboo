package com.wei.generator.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorRequestModel {

    private String projectPath;
    private String parentPackagePath;
    private String packagePath;
    //表名
    private String targetName;
    private String htmlModelName;

    private String controllerStatus;
    private String controllerName;
    private String mapperStatus;
    private String mapperName;
    private String mapperXmlStatus;
    private String mapperXmlName;
    private String serviceStatus;
    private String serviceName;
    private String implStatus;
    private String implName;
    private String htmlStatus;
    private String htmlName;
    private String dtoStatus;
    private String dtoName;

}
