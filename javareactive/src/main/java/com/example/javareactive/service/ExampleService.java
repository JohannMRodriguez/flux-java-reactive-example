package com.example.javareactive.service;

import com.example.javareactive.dto.RequestExternalServiceDto;
import com.example.javareactive.dto.ResponseExternalServiceDto;
import com.example.javareactive.dto.ResponseFluxExampleDto;
import com.example.javareactive.dto.ResponseSimpleExampleDto;
import com.example.javareactive.gateway.ExampleRestInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import retrofit2.Response;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {
    private final ExampleRestInterface restInterface;

    private int maxThreadCount = 0;
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public ResponseSimpleExampleDto callExternalServiceWithoutFlux(RequestExternalServiceDto request) throws IOException {

        var stopWatch = new StopWatch();
        stopWatch.start();

        var response = new ArrayList<String>();

        for (int i = 0; i < request.getDataList().toArray().length; i++) {

            maxThreadCount = getTheCurrentThreadsInUse();

            try {
                Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(request.getDataList().get(i)).execute();
                response.add(httpResponse.body().getJson());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        stopWatch.stop();
        log.info("tiempo ejecutando SIN java reactivo -> {}ms", stopWatch.getTotalTimeMillis());
        log.info("nro maximo de threads usados en el proceso -> {}", maxThreadCount);

        return new ResponseSimpleExampleDto(response);
    }
    
    public ResponseFluxExampleDto callExternalServiceWithFlux(RequestExternalServiceDto request) throws IOException {

        var stopWatch = new StopWatch();
        stopWatch.start();

        List<String> response = new ArrayList<>();

        Flux.fromIterable(request.getDataList())
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::callExternalApi)
                .sequential()
                .doOnNext(response::add)
                .blockLast();

        stopWatch.stop();
        log.info("tiempo ejecutando CON java reactivo -> {}ms", stopWatch.getTotalTimeMillis());
        log.info("nro maximo de threads usados en el proceso -> {}", maxThreadCount);

        return new ResponseFluxExampleDto(response);
    }

    private Mono<String> callExternalApi(String each) {

        maxThreadCount = getTheCurrentThreadsInUse();

        return Mono.fromCallable(() -> {
            Response<ResponseExternalServiceDto> httpResponse = restInterface.callExternalService(each).execute();
            return httpResponse.body().getJson();

        }).subscribeOn(Schedulers.boundedElastic());
    }

//    ---------------------------------------------------------------------------------------------------

    private int getTheCurrentThreadsInUse() {

        int currentThreadCount = threadMXBean.getThreadCount();

        return Math.max(currentThreadCount, maxThreadCount);
    }
}
