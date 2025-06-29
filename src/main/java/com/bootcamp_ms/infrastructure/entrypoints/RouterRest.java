package com.bootcamp_ms.infrastructure.entrypoints;

import com.bootcamp_ms.infrastructure.entrypoints.handler.BootcampHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(BootcampHandler bootcampHandler) {
        return route(POST("/bootcamp"), bootcampHandler::saveBootcamp)
                .andRoute(GET("/bootcamp/list"), bootcampHandler::listBootcamps)
                .andRoute(GET("/bootcamp/popular"), bootcampHandler::getMostPopularBootcamp)
                .andRoute(GET("/bootcamp/{id}"), bootcampHandler::getBootcampById)
                .andRoute(DELETE("/bootcamp/delete"), bootcampHandler::deleteBootcamp);
    }

}
