package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxFilterTest {

    List<String> namesList = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void fluxFilterTest() {
        Flux<String> stringFlux = Flux.fromIterable(namesList)
                .filter(s -> s.startsWith("a"));

        StepVerifier.create(stringFlux.log())
                .expectNext("adam", "anna")
                .verifyComplete();
    }
}
