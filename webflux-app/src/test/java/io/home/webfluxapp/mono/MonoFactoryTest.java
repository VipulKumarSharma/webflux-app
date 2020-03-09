package io.home.webfluxapp.mono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Supplier;

public class MonoFactoryTest {

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<Object> mono = Mono.justOrEmpty(null); //Mono.empty();

        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> stringSupplier = () ->"adam";
        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono.log())
                .expectNext("adam")
                .verifyComplete();
    }

}
