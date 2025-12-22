package com.exchange.gateway.config;


import com.exchange.gateway.filter.MdcContextLifter;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

import jakarta.annotation.PostConstruct;

@Configuration
public class ReactorMdcConfiguration {
    private static final String HOOK_KEY = "mdc";
    @PostConstruct
    public void init() {
        Hooks.onEachOperator(
                "mdc",
                Operators.lift((sc, sub)
                        -> new MdcContextLifter<>(sub))
        );
    }




}