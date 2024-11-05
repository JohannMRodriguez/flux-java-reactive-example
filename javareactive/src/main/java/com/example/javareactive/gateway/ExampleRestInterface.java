package com.example.javareactive.gateway;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExampleRestInterface {
    @POST("https://httpbin.org/anything")
    Call<ResponseExternalServiceDto> callExternalService(@Body String request);
}
