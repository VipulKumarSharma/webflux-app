package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxFactoryTest {

    List<String> namesList = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(namesList);

        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] namesArray = new String[]{"adam", "anna", "jack", "jenny"};
        Flux<String> namesFlux = Flux.fromArray(namesArray);

        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(namesList.stream());

        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1,5);

        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

}
