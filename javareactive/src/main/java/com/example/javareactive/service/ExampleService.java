package com.example.javareactive.service;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.gateway.ExampleRestInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private ExampleRestInterface restInterface;

    public ResponseExternalServiceDto callExternalServiceWithoutFlux(RequestExternalServiceDto requestList) {

        return new ResponseExternalServiceDto();
    }
}
