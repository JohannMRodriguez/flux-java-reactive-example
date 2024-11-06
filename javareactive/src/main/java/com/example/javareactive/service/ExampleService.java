package com.example.javareactive.service;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.gateway.ExampleRestInterface;
import kotlin.collections.builders.MapBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRestInterface restInterface;

    public ResponseExternalServiceDto callExternalServiceWithoutFlux(RequestExternalServiceDto request) throws IOException {

        var responseList = new HashMap<String, String>();
        var response = new ResponseExternalServiceDto();

        request.getJson().forEach(
                each -> {
                    try {
                        System.out.println(each);
                        var httpResponse = restInterface.callExternalService(each).execute().body();
                        responseList.put(each, httpResponse.getJson().toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        response.setJson(responseList);
        return response;
    }
//    public ResponseExternalServiceDto callExternalServiceWithFlux(RequestExternalServiceDto request) throws IOException {
//
//        List<String> status = new ArrayList<>();
//
//        Flux.fromIterable(request.getRequestExample())
//                .parallel()
//                .runOn(Schedulers.parallel())
//                .flatMap(this::callExternalApi)
//                .sequential()
//                .doOnNext(status::add)
//                .blockLast();
//
//        System.out.println(status);
//
//        return new ResponseExternalServiceDto();
//    }
//
//    private Mono<String> callExternalApi(String each) {
//
//        return Mono.fromCallable(() -> {
//            Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(each).execute();
//            return httpResponse.body().getJson().getJson();
//
//        }).subscribeOn(Schedulers.boundedElastic());
//    }
}
