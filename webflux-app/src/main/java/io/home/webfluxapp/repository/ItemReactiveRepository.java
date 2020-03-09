package io.home.webfluxapp.repository;

import io.home.webfluxapp.persistence.Item;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveCrudRepository<Item, String> {

    @Query("SELECT * FROM ITEM WHERE DESCRIPTION = :description")
    Flux<Item> findByDescription(String description);

}
