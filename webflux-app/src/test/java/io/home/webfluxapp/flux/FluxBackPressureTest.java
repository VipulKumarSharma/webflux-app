package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1, 10);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()

                .thenRequest(1)
                .expectNext(1)

                .thenRequest(1)
                .expectNext(2)

                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(
                i -> System.out.println("Element is : "+i),
                e -> System.err.println("Exception is : "+e.getMessage()),
                () -> System.out.println("Done"),
                subscription -> subscription.request(2) // After emitting 2 elements it closes
        );
    }

    @Test
    public void backPressure_cancel() {
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(
                i -> System.out.println("Element is : "+i),
                e -> System.err.println("Exception is : "+e.getMessage()),
                () -> System.out.println("Done"),
                subscription -> subscription.cancel() // Cancels the subscription
        );
    }

    @Test
    public void customized_backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                // Only method needed if wants to do data level validation
                request(1); // request values one by one
                System.out.println("Value received is : "+value);

                if(value == 4) {
                    cancel();
                }
            }
        });
    }
}
