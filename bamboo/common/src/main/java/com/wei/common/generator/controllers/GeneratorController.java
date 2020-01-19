package com.wei.common.generator.controllers;

import com.wei.common.generator.model.GeneratorRequestModel;
import com.wei.common.generator.service.IGeneratorService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/generator")
public class GeneratorController {

    private final IGeneratorService generatorService;

    public GeneratorController(IGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    /**
     * 读取表格中字段信息，创建对应的模板文件
     *
     * @param generatorInfo 请求参数，包括表名和生成文件的目录信息
     * @return  执行结果
     */
    @PutMapping(value = "/generate")
    public Boolean generatorTables(@RequestBody GeneratorRequestModel generatorInfo) {
        return generatorService.generatorFile(generatorInfo);
    }

}
