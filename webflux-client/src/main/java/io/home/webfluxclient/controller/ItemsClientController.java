package io.home.webfluxclient.controller;

import io.home.webfluxclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemsClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    static final String itemsEndpoint = "/functional/v1/items";

    @GetMapping("/items/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient
                .get()
                .uri(itemsEndpoint)
                .retrieve()
                .bodyToFlux(Item.class)
                .log("getAllItemsUsingRetrieve");
    }

    @GetMapping("/items/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient
                .get()
                .uri(itemsEndpoint)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("getAllItemsUsingExchange");
    }

    @GetMapping("/items/{id}/retrieve")
    public Mono<Item> getItemByIdUsingRetrieve(@PathVariable String id) {
        return webClient
                .get()
                .uri(itemsEndpoint+"/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("getItemByIdUsingRetrieve");
    }

    @GetMapping("/items/{id}/exchange")
    public Mono<Item> getItemByIdUsingExchange(@PathVariable String id) {
        return webClient
                .get()
                .uri(itemsEndpoint+"/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("getItemById");
    }

    @PostMapping("/items")
    public Mono<Item> createItem(@RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);

        return webClient
                .post()
                .uri(itemsEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    itemMono,
                    Item.class
                )
                .retrieve()
                .bodyToMono(Item.class)
                .log("createItem");
    }

    @PutMapping("/items/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);

        return webClient
                .put()
                .uri(itemsEndpoint+"/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("updateItem");
    }

    @DeleteMapping("/items/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {

        return webClient
                .delete()
                .uri(itemsEndpoint+"/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .log("deleteItem");
    }
}
