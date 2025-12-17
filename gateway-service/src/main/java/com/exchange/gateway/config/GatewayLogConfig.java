package com.exchange.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayLogConfig {

    @Bean
    public TraceGlobalFilter traceGlobalFilter() {
        return new TraceGlobalFilter();
    }

    @Bean
    public ReactorMdcConfiguration reactorMdcConfiguration() {
        return new ReactorMdcConfiguration();
    }
}