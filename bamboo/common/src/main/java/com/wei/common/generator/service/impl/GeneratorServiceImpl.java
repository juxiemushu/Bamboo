package com.wei.common.generator.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.wei.common.generator.model.DataSourceModel;
import com.wei.common.generator.model.GeneratorRequestModel;
import com.wei.common.generator.service.IGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeneratorServiceImpl implements IGeneratorService {

    private final DataSourceModel dataSourceModel;

    public GeneratorServiceImpl(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public Boolean generatorFile(GeneratorRequestModel requestModel) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        log.info("项目路径: {}", projectPath);

        gc.setOutputDir(projectPath + "/" + requestModel.getProjectModule() + "/src/main/java");
        gc.setAuthor(StringUtils.isBlank(requestModel.getAuthor()) ? "Auto-generate" : requestModel.getAuthor());
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dataSourceModel.getDatabaseUrl());
        // dsc.setSchemaName("public");
        dsc.setDriverName(dataSourceModel.getDatabaseDriverName());
        dsc.setUsername(dataSourceModel.getUsername());
        dsc.setPassword(dataSourceModel.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // 基础路径，格式为：com.wei.common
        pc.setParent(requestModel.getBasePackage());
        pc.setModuleName(requestModel.getModule());
        mpg.setPackageInfo(pc);

        // 自定义属性注入
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("package", "com.wei.common.cased.service");
                List<String> imports = new ArrayList<>();
                imports.add("com.baomidou.mybatisplus.core.mapper.BaseMapper");
                map.put("import", imports);
                map.put("entityName", "BambooUser");
                map.put("serviceName", "BambooUserService");
                this.setMap(map);
            }
        };

        // 自定义输出配置(xml文件路径和java文件路径不一样，需要自定义)
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 freemarker
        String xmlTemplate = "/templates/mapper.xml.ftl";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(xmlTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/" + requestModel.getProjectModule() + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("/templates/entity");
        templateConfig.setMapper("/templates/mapper");
        templateConfig.setService("/templates/service");
        templateConfig.setServiceImpl("/templates/serviceImpl");
        templateConfig.setController("/templates/controller");
        // xml文件路径在resource目录下，这里设置为 null，否则会在java路径下生成xml文件
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(requestModel.getTableNames().toArray(String[]::new));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

        return Boolean.TRUE;
    }

}
