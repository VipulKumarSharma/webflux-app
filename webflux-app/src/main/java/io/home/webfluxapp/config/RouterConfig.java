package io.home.webfluxapp.config;

import io.home.webfluxapp.handler.AppRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(AppRequestHandler handler) {
        return RouterFunctions
            .route(
                GET("/functional/flux").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                handler::flux
            )
            .andRoute(
                GET("/functional/mono").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                handler::mono
            );
    }
}
