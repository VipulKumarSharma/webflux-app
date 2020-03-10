package io.home.webfluxapp.controller.v1;

import io.home.webfluxapp.common.AppConstants;
import io.home.webfluxapp.persistence.Item;
import io.home.webfluxapp.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemController {

    final ItemReactiveRepository itemRepo;
    public ItemController(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping(AppConstants.ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {
        return itemRepo.findAll();
    }

    @GetMapping(AppConstants.ITEM_ENDPOINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
        return itemRepo.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(AppConstants.ITEM_ENDPOINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemRepo.save(item);
    }

    @DeleteMapping(AppConstants.ITEM_ENDPOINT_V1+"/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return itemRepo.deleteById(id);
    }

    @PutMapping(AppConstants.ITEM_ENDPOINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemRepo.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setDescription(item.getDescription());
                    currentItem.setPrice(item.getPrice());
                    return itemRepo.save(currentItem);
                })
                .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /*@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("ExceptionHandler: handleRuntimeException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }*/

    @GetMapping(AppConstants.ITEM_ENDPOINT_V1+"/error")
    public Flux<Item> generateError() {
        return itemRepo
                .findAll()
                .concatWith(Mono.error(new RuntimeException("Intentional Runtime Exception")));
    }
}
