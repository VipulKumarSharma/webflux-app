package io.home.webfluxapp.controller.v1;

import io.home.webfluxapp.common.AppConstants;
import io.home.webfluxapp.persistence.Item;
import io.home.webfluxapp.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemControllerTest {

    /** For Non-Blocking Endpoint, we must have Non-Blocking client
     ** By Default CommandLineRunner will also be executed.
     ** To stop CLR execution use different profile.
     ** @SpringBootTest will initialize ItemReactiveRepository
     **/

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemRepo;

    @Before
    public void setUp() {
        itemRepo.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepo::save)
                .doOnNext(item -> {
                    System.out.println("Inserted Item is : "+item);
                })
                .blockLast();
        /** blockLast() -> To setup data before any Test runs **/
    }

    public List<Item> getItems() {
        return List.of(
                new Item(null, "Item 5", 555.55),
                new Item(null, "Item 6", 666.66),
                new Item(null, "Item 7", 777.77),
                new Item(null, "Item 8", 888.88)
        );
    }

    @Test
    public void getAllItems_approach1() {
        webTestClient
                .get()
                .uri(AppConstants.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4);

    }

    @Test
    public void getAllItems_approach2() {
        webTestClient
                .get()
                .uri(AppConstants.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(listEntityExchangeResult -> {
                    listEntityExchangeResult.getResponseBody().forEach(item -> {
                        assertTrue(item.getId() != null);
                    });
                });
    }

    @Test
    public void getAllItems_approach3() {
        Flux<Item> itemFlux = webTestClient
                .get()
                .uri(AppConstants.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux.log("getAllItems_approach3"))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        webTestClient
                .get()
                .uri(AppConstants.ITEM_ENDPOINT_V1.concat("/{id}"), "5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 555.55); // Price of the particular Item
    }

    @Test
    public void getItemById_notFound() {
        webTestClient
                .get()
                .uri(AppConstants.ITEM_ENDPOINT_V1.concat("/{id}"), "55")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void saveItem() {
        Item item = new Item (null, "Item Y", 999.99);

        webTestClient
                .post()
                .uri(AppConstants.ITEM_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item).log("saveItem"), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Item Y")
                .jsonPath("$.price").isEqualTo(999.99);
    }

    @Test
    public void deleteItemById() {
        webTestClient
                .delete()
                .uri(AppConstants.ITEM_ENDPOINT_V1.concat("/{id}"), "5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        Item item = new Item (null, "Item #6", 600.00);

        webTestClient
                .put()
                .uri(AppConstants.ITEM_ENDPOINT_V1.concat("/{id}"), "6")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item).log("updateItem"), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("6")
                .jsonPath("$.description").isEqualTo("Item #6")
                .jsonPath("$.price").isEqualTo(600.00);
    }

    @Test
    public void updateItem_notFound() {
        Item item = new Item (null, "Item #6", 600.00);

        webTestClient
                .put()
                .uri(AppConstants.ITEM_ENDPOINT_V1.concat("/{id}"), "10")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item).log("updateItem_notFound"), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
