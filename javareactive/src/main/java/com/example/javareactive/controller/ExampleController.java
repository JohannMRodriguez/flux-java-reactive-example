package com.example.javareactive.controller;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/example")
@Validated
public class ExampleController {

    @Autowired
    private ExampleService service;

    @PostMapping
    public ResponseExternalServiceDto exampleWithoutFlux(
            @RequestBody RequestExternalServiceDto request
            ) {

        return service.callExternalServiceWithoutFlux(request);
    }
}
