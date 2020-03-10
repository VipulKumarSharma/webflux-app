package io.home.webfluxapp.config;

import io.home.webfluxapp.common.AppConstants;
import io.home.webfluxapp.handler.AppRequestHandler;
import io.home.webfluxapp.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(AppRequestHandler appHandler, ItemsHandler itemsHandler) {
        return RouterFunctions
            .route(
                GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)),
                appHandler::flux
            )
            .andRoute(
                GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)),
                appHandler::mono
            )
            .andRoute(
                GET("/functional/stream").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                appHandler::streaming
            )
            .andRoute(
                GET(AppConstants.ITEM_FUNC_ENDPOINT_V1).and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::getAllItems
            )
            .andRoute(
                GET(AppConstants.ITEM_FUNC_ENDPOINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::getItemById
            )
            .andRoute(
                POST(AppConstants.ITEM_FUNC_ENDPOINT_V1).and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::saveItem
            )
            .andRoute(
                DELETE(AppConstants.ITEM_FUNC_ENDPOINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::deleteItemById
            )
            .andRoute(
                PUT(AppConstants.ITEM_FUNC_ENDPOINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::updateItemById
            )
            .andRoute(
                GET("/functional/error").and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::generateError
            );
    }
}
