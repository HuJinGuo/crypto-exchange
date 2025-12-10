package com.exchange.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09  22:13
 * @Description: TODO
 * @Version: 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SettlementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class, args);
        System.out.println("settlement application started");
    }
}
