package com.exchange.settlement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09 22:32
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "settlement-service pong";
    }
}
