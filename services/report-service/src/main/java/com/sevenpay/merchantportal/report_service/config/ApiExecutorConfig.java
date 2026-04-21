package com.sevenpay.merchantportal.report_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApiExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService pageExecutor() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService transactionExecutor() {
        return Executors.newFixedThreadPool(50);
    }
}
