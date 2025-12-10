package com.exchange.gateway;

import jakarta.annotation.PostConstruct;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DebugConfig {

    private final DiscoveryClient discoveryClient;

    public DebugConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @PostConstruct
    public void printInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        System.out.println("==== DISCOVERY DEBUG ====");
        System.out.println("user-service instances size = " + instances.size());
        for (ServiceInstance instance : instances) {
            System.out.println(" -> " + instance.getHost() + ":" + instance.getPort());
        }
        System.out.println("==== DISCOVERY DEBUG END ====");
    }
}