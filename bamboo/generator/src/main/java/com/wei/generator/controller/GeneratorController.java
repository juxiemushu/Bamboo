package com.wei.generator.controller;

import com.wei.generator.model.request.GeneratorRequestModel;
import com.wei.generator.service.IGeneratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/generator")
public class GeneratorController {

    private final IGeneratorService generatorService;

    public GeneratorController(IGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @PostMapping
    public int generatorTables(@RequestBody GeneratorRequestModel generatorInfo) {
        return generatorService.generatorFile(generatorInfo);
    }

}
