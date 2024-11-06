package com.example.javareactive.service;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.dto.ResponseFluxExampleDto;
import com.example.javareactive.dto.ResponseSimpleExampleDto;
import com.example.javareactive.gateway.ExampleRestInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRestInterface restInterface;

    public ResponseSimpleExampleDto callExternalServiceWithoutFlux(RequestExternalServiceDto request) throws IOException {

        var response = new ArrayList<String>();

        request.getDataList().forEach(
                data -> {

                    try {
                        Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(data).execute();
                        response.add(httpResponse.body().getJson());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return new ResponseSimpleExampleDto(response);
    }
    public ResponseFluxExampleDto callExternalServiceWithFlux(RequestExternalServiceDto request) throws IOException {

        List<String> response = new ArrayList<>();

        Flux.fromIterable(request.getDataList())
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::callExternalApi)
                .sequential()
                .doOnNext(response::add)
                .blockLast();

        System.out.println(response);

        return new ResponseFluxExampleDto(response);
    }

    private Mono<String> callExternalApi(String each) {

        return Mono.fromCallable(() -> {
            Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(each).execute();
            return httpResponse.body().getJson();

        }).subscribeOn(Schedulers.boundedElastic());
    }
}
