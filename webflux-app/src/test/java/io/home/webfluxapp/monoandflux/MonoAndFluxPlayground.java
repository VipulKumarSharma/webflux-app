package io.home.webfluxapp.monoandflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class MonoAndFluxPlayground {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
            //.concatWith(Flux.error(new RuntimeException("Exception occurred")))
            .concatWith(Flux.just("After Error"))
            .log();

        stringFlux
            .subscribe(
                System.out::println,
                (e) -> System.err.println(e.getMessage()),
                () -> System.out.println("Subscription Completed")
            );
    }
}
