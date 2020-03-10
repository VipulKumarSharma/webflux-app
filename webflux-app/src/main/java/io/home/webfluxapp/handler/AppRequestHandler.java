package io.home.webfluxapp.handler;

import io.home.webfluxapp.persistence.Item;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class AppRequestHandler {

    public Mono<ServerResponse> flux(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Flux.range(1,4).log(),
                        Integer.class
                );
    }

    public Mono<ServerResponse> mono(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(1).log(),
                        Integer.class
                );
    }

    public Mono<ServerResponse> streaming(ServerRequest request) {
        Flux<Item> longIntervalFlux = Flux.interval(Duration.ofSeconds(1))
                .map(aLong -> new Item(aLong.toString(), "Item #"+aLong, 100.00+aLong));

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(longIntervalFlux.log("Streaming..."), Item.class);
    }
}
