package io.home.webfluxapp.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
/** @WebFluxTest scans @Controller, NOT @Component / @Service / @Repository **/
public class ReactiveControllerTest {

    @Autowired
    WebTestClient webTestClient; /** @WebFluxTest creates it's instance **/

    @Test
    public void flux_approach1() {
        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/flux")
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
    public void flux_approach2() {
        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void flux_approach3() {
        List<Integer> expectedList = List.of(1,2,3,4);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(
                expectedList,
                listEntityExchangeResult.getResponseBody()
        );
    }

    @Test
    public void flux_approach4() {
        List<Integer> expectedList = List.of(1,2,3,4);

        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
                });
    }

    @Test
    public void fluxStreamTest() {
        Flux<Long> longStreamFlux = webTestClient
                .get()
                .uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longStreamFlux)
                .expectNext(0l)
                .expectNext(1l)
                .expectNext(2l)
                .thenCancel()
                .verify();


    }

    @Test
    public void monoTest() {
        Integer expectedValue = 1;

        webTestClient
                .get()
                .uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(integerEntityExchangeResult -> {
                    assertEquals(
                            expectedValue,
                            integerEntityExchangeResult.getResponseBody()
                    );
                });
    }
}
