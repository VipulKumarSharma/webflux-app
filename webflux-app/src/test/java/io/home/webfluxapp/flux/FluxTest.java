package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

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

    @Test
    public void fluxTestElements_WithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred")
                .verify();
    }

    @Test
    public void fluxElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occurred")
                .verify();
    }
}
