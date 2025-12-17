package com.exchange.common.datasource.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
@MapperScan(basePackages = "com.exchange.**.mapper")
public class DataSourceAutoConfig {
}
