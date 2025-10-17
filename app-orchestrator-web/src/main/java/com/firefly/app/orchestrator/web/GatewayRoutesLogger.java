package com.firefly.app.orchestrator.web;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesLogger {

    @Bean
    public ApplicationRunner showRoutes(RouteLocator routeLocator) {
        return args -> {
            routeLocator.getRoutes().subscribe(route -> {
                System.out.println("Route ID: " + route.getId());
                System.out.println("URI: " + route.getUri());
                System.out.println("Predicates: " + route.getPredicate());
                System.out.println("Filters: " + route.getFilters());
                System.out.println("-----------------------------");
            });
        };
    }
}