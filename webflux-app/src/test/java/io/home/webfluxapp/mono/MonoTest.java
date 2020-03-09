package io.home.webfluxapp.mono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    @Test
    public void monoTest () {
        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_WithError () {
        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class)
                .verify();

        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
