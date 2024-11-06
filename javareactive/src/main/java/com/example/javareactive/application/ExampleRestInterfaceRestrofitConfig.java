package com.example.javareactive.application;

import com.example.javareactive.gateway.ExampleRestInterface;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ExampleRestInterfaceRestrofitConfig {
    @Value("${base.url}")
    private String url;
    @Value("${connectTimeout:40}")
    private long connectTimeout;
    @Value("${readTimeout:40}")
    private long readTimeout;

    public static final String GENERANDO_CLIENTE_DE_RETROFIT = "Generando cliente de retrofit primitivas";

    @Bean
    public ExampleRestInterface getExampleRestInterface() {

        log.info(GENERANDO_CLIENTE_DE_RETROFIT + " para el servicio de primitivas [{}]", url);

        val client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
//                .addInterceptor(logging)
//                .addInterceptor(primitivasAuthInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ExampleRestInterface.class);
    }
}
