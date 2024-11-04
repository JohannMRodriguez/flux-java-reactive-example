package com.example.javareactive.gateway;

import com.example.javareactive.dto.ResponseExternalServiceDto;
import retrofit2.http.POST;

public interface ExampleRestInterface {

    @POST("/example")
    public ResponseExternalServiceDto callExternalService();
}
