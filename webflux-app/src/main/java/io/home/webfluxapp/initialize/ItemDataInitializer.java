package io.home.webfluxapp.initialize;

import io.home.webfluxapp.persistence.Item;
import io.home.webfluxapp.repository.ItemReactiveRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    final ItemReactiveRepository itemRepo;
    public ItemDataInitializer(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeDataSetup();
    }

    public List<Item> getItems() {
        return List.of(
            new Item(null, "Item 5", 555.55),
            new Item(null, "Item 6", 666.66),
            new Item(null, "Item 7", 777.77),
            new Item(null, "Item 8", 888.88)
        );
    }

    private void initializeDataSetup() {
        itemRepo.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepo::save)
                .thenMany(itemRepo.findAll())
                .subscribe(item -> {
                    System.out.println("Item inserted from CLR : " + item);
                });
    }

}
