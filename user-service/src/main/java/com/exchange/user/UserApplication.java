package com.exchange.user;

import com.exchange.common.redis.annotation.EnableCommonRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09 21:51
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableCommonRedis
@ComponentScan(basePackages = {
        "com.exchange.user",
        "com.exchange.common.datasource"
})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("user application started");

    }
}