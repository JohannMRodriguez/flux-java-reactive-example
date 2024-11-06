package com.example.javareactive.service;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
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

    public ResponseExternalServiceDto callExternalServiceWithoutFlux(RequestExternalServiceDto request) throws IOException {

        var response = new ArrayList<>();

        request.getJson().forEach(
                each -> {
                    try {
                        Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(each).execute();
//                        response.add(httpResponse.body().getJson());
                        System.out.println(httpResponse.body().getJson());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        System.out.println(response);

        return new ResponseExternalServiceDto();
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
