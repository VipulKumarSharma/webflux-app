package io.home.webfluxapp.handler;

import io.home.webfluxapp.persistence.Item;
import io.home.webfluxapp.repository.ItemReactiveRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

    final ItemReactiveRepository itemRepo;
    public ItemsHandler(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    itemRepo.findAll(),
                    Item.class
                );
    }

    public Mono<ServerResponse> getItemById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Item> itemMono = itemRepo.findById(id);

        return itemMono
                .flatMap(item ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item))
                )
                .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> saveItem(ServerRequest request) {
        Mono<Item> itemMono = request.bodyToMono(Item.class);

        return itemMono
                .flatMap(item ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                            itemRepo.save(item),
                            Item.class
                        )
                );
    }

    public Mono<ServerResponse> deleteItemById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> voidMono = itemRepo.deleteById(id);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    voidMono,
                    Void.class
                );
    }

    public Mono<ServerResponse> updateItemById(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(Item.class)
                .flatMap(item ->
                    itemRepo.findById(id)
                        .flatMap(dbItem -> {
                            dbItem.setDescription(item.getDescription());
                            dbItem.setPrice(item.getPrice());
                            return itemRepo.save(dbItem);
                        })
                )
                .flatMap(dbItem ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(dbItem))
                )
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> generateError(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    Mono.error(new RuntimeException("Intentional Runtime Exception")),
                    RuntimeException.class
                );
    }
}
