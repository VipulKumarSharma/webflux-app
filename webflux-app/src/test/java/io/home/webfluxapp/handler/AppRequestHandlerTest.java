package io.home.webfluxapp.handler;

import io.home.webfluxapp.persistence.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class AppRequestHandlerTest {

    /** @SpringBootTest can scan @Component NOT @WebFluxTest **/

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTesting() {
        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void monoTesting() {
        Integer expectedValue = 1;

        webTestClient
                .get()
                .uri("/functional/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(integerEntityExchangeResult -> {
                    assertEquals(expectedValue, integerEntityExchangeResult.getResponseBody());
                });
    }

    @Test
    public void streaming() {
        Flux<Item> itemFlux = webTestClient
                .get()
                .uri("/functional/stream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class)
                .getResponseBody()
                .take(5);

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}
