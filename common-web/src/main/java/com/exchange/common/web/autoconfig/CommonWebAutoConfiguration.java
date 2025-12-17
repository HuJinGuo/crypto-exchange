package com.exchange.common.web.autoconfig;


import com.exchange.common.web.exception.MvcGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CommonWebAutoConfiguration {


    @Bean
    @ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
    public MvcGlobalExceptionHandler mvcGlobalExceptionHandler(){
        return new MvcGlobalExceptionHandler();
    }
}
