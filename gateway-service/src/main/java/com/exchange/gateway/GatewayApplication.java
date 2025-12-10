package com.exchange.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09  17:29
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class,
        org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.class,
//        org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration.class
})
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("gateway application started");
    }

    @Bean
    public CommandLineRunner printConfig(Environment env) {
        return args -> {
            System.out.println("==== DEBUG ROUTES ====");
            System.out.println(env.getProperty("spring.cloud.gateway.routes[0].id"));
            System.out.println(env.getProperty("spring.cloud.gateway.routes[0].uri"));
        };
    }
}
