package com.example.javareactive.controller;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/example")
@Validated
public class ExampleController {

    @Autowired
    private ExampleService service;

    @PostMapping("/1")
    public ResponseExternalServiceDto exampleWithoutFlux(
            @RequestBody RequestExternalServiceDto request
            ) throws IOException {

        return service.callExternalServiceWithoutFlux(request);
    }

    @PostMapping("/2")
    public ResponseExternalServiceDto exampleWithFlux(
            @RequestBody RequestExternalServiceDto request
    ) throws IOException {

        return service.callExternalServiceWithFlux(request);
    }
}
