package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxTransformTest {

    List<String> namesList = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void fluxTransformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(namesList)
                .map(String::toUpperCase);

        StepVerifier.create(stringFlux.log())
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingMap_Length_repeat() {
        Flux<Integer> integerFlux = Flux.fromIterable(namesList)
                .map(String::length)
                .repeat(1);

        StepVerifier.create(integerFlux.log())
                .expectNext(4,4,4,5, 4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingMap_Filter() {
        Flux<String> stringFlux = Flux.fromIterable(namesList)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase);

        StepVerifier.create(stringFlux.log())
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(List.of("A","B","C","D","E"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)));

        StepVerifier.create(stringFlux.log())
                .expectNextCount(10)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(s, "newValue");
    }

    @Test
    public void fluxTransformUsingFlatMap_Parallel() {
        Flux<String> stringFlux
                = Flux.fromIterable(List.of("A","B","C","D","E")) // Flux<String>
                .window(2) // Wait for 2 elements & gives Flux< Flux<String> > -> (A,B), (C,D), (E)
                .flatMap(s ->
                    s.map(this::convertToList).subscribeOn(Schedulers.parallel()) // Flux<List<String>>
                )
                .flatMap(s -> Flux.fromIterable(s)); // Flux<String>

        StepVerifier.create(stringFlux.log())
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingFlatMap_Parallel_maintain_order() {
        Flux<String> stringFlux
                = Flux.fromIterable(List.of("A","B","C","D","E")) // Flux<String>
                .window(2)          // Wait for 2 elements & gives Flux< Flux<String> > -> (A,B), (C,D), (E)
                //.concatMap(s ->   // Sequential but slow
                .flatMapSequential(s ->
                    s.map(this::convertToList).subscribeOn(Schedulers.parallel())  // Flux<List<String>>
                )
                .flatMap(s -> Flux.fromIterable(s)); // Flux<String>

        StepVerifier.create(stringFlux.log())
                .expectNextCount(10)
                .verifyComplete();
    }
}
