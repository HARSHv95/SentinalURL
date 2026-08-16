package com.harsh.sentinal.scan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Bounds concurrency for @Async work (scan processing, watchlist checks,
 * Gmail sync). Without a configured Executor here, Spring falls back to
 * SimpleAsyncTaskExecutor, which spawns an unbounded new thread per task —
 * fine for one-off scans, but a Gmail sync can enqueue dozens of scans at
 * once, and unbounded concurrency just means every external threat-provider
 * API gets hammered simultaneously and rate-limited (e.g. VirusTotal's free
 * tier is ~4 req/min) rather than actually finishing faster.
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 8;
    private static final int QUEUE_CAPACITY = 200;

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.warn("Uncaught async exception in {}", method.getName(), ex);
    }
}
