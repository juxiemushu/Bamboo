package com.wei.common.generator.model;

import lombok.Data;

@Data
public class FileInfoModel {

    private String fileType;
    private String fileName;
    private Boolean createFlag;
    private Boolean coverFlag;

}
