package com.exchange.order;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09  22:05
 * @Description: TODO
 * @Version: 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
       SpringApplication.run(OrderApplication.class, args);
        System.out.println("order application started");
    }
}
