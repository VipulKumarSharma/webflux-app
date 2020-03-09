package io.home.webfluxapp.repository;

import io.home.webfluxapp.persistence.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemRepo;

    List<Item> items = List.of(
            new Item(null, "Item 5", 500.55),
            new Item(null, "Item 6", 600.66),
            new Item(null, "Item 7", 700.77)
    );

    @Before
    public void setUpData() {
        itemRepo.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepo::save)
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemRepo.findAll().log("findAll"))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemRepo.findById("7").log("findById"))
                .expectSubscription()
                .expectNextMatches(item -> "Item 7".equals(item.getDescription()))
                .verifyComplete();
    }

    @Test
    public void getItemByDesc() {
        StepVerifier.create(itemRepo.findByDescription("Item 7").log("findItemByDescription"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Item X", 111.11);
        Mono<Item> itemMono = itemRepo.save(item);

        StepVerifier.create(itemMono.log("saveItem"))
                .expectSubscription()
                .expectNextMatches(item1 -> (item.getId() != null && "Item X".equals(item.getDescription())))
                .verifyComplete();

    }

    @Test
    public void updateItem() {
        Flux<Item> updatedItem = itemRepo.findByDescription("Item 5")
                .map(item -> {
                    item.setPrice(555.55);
                    return item;
                })
                .flatMap(itemRepo::save);

        StepVerifier.create(updatedItem.log("updateItem"))
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 555.55)
                .verifyComplete();
    }

    @Test
    public void deleteItemById() {
        Mono<Void> deletedItem = itemRepo.findById("5")
                .map(Item::getId)
                .flatMap(itemRepo::deleteById);

        StepVerifier.create(deletedItem.log("deleteItemById"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepo.findAll().log("newItemList"))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void deleteItem() {
        Flux<Void> deletedItems = itemRepo.findByDescription("Item 5")
                .flatMap(itemRepo::delete);

        StepVerifier.create(deletedItems.log("deleteItem"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepo.findAll().log("newItemList"))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }
}