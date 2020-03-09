package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxCombineTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> stringFlux1 = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergeFlux = Flux.merge(stringFlux1, stringFlux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay() {
        Flux<String> stringFlux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.merge(stringFlux1, stringFlux2); // NO Order

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                //.expectNext("A","B","C","D","E","F")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat() {
        Flux<String> stringFlux1 = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergeFlux = Flux.concat(stringFlux1, stringFlux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay() {
        Flux<String> stringFlux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.concat(stringFlux1, stringFlux2); // Order Preserved F1 -> F2

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingZip() {
        Flux<String> stringFlux1 = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F", "G");

        Flux<String> mergeFlux = Flux.zip(stringFlux1, stringFlux2, (t1, t2) -> {
            // A,D : B,E : C,F
            return t1.concat(t2);
        });

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
}
