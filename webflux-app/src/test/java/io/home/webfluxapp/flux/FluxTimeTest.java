package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).log();
            // Emit value each 200 msec, until method exists [0 -> ...]
            // Executed in parallel thread

        infiniteFlux.subscribe(aLong -> {
            System.out.println("Value is : "+aLong);
        });

        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMapTest() throws InterruptedException {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .map(l -> l.intValue())
                .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMapTest_withDelay() throws InterruptedException {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(2))
                .map(l -> l.intValue())
                .take(3);

        StepVerifier.create(finiteFlux.log())
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

}
