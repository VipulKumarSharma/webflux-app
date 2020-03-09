package io.home.webfluxapp.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotAndColdPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        // Cold Publisher -> every subscriber emits value from beginning

        stringFlux.subscribe(s -> System.out.println("Subscriber 1  : "+s));
        Thread.sleep(2000);

        stringFlux.subscribe(s -> System.out.println("Subscriber 2  : "+s));
        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        // Hot Publisher -> Doesn't emit values from beginning

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect(); // enable Hot Publishing

        connectableFlux.subscribe(s -> System.out.println("Subscriber 1  : "+s));
        Thread.sleep(3000);

        connectableFlux.subscribe(s -> System.out.println("Subscriber 2  : "+s));
        Thread.sleep(4000);
    }
}
