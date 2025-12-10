package com.exchange.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteDebugger {

    @Autowired
    RouteDefinitionLocator locator;

    @Bean
    public Object printRoutes() {
        locator.getRouteDefinitions().subscribe(route -> {
            System.out.println("==== FOUND ROUTE ====");
            System.out.println(route);
        });
        return null;
    }
}