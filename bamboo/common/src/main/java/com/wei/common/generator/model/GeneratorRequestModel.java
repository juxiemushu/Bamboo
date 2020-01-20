package com.wei.common.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorRequestModel {

    @NotEmpty
    private String projectModule;

    @NotEmpty
    @Length(min = 0, max = 255)
    private String basePackage;

    @NotEmpty
    private String module;

    private String author;

    @NotEmpty
    private String tableName;

    private List<FileInfoModel> fileModelList;

}
