package com.wei.common.generator.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class DataSourceModel {

    @Value(value = "${spring.datasource.url}")
    private String databaseUrl;

    @Value(value = "${spring.datasource.driverClassName}")
    private String databaseDriverName;

    @Value(value = "${spring.datasource.username}")
    private String username;

    @Value(value = "${spring.datasource.password}")
    private String password;

}
