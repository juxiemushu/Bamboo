package com.wei.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableModel {

    private String name;
    private List<ColumnModel> columns = new ArrayList();
    private boolean isMultiLanguage = false;

}
