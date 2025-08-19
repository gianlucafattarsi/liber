package com.github.gianlucafattarsi.liberapi.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {
    
    /**
     * Configures an asynchronous task executor with a thread pool.
     * The executor is wrapped in a DelegatingSecurityContextAsyncTaskExecutor
     * to ensure that the security context is propagated to the async tasks.
     *
     * @return a configured DelegatingSecurityContextAsyncTaskExecutor
     */
    @Bean(name = "threadPoolExecutor")
    public DelegatingSecurityContextAsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(100);
        executor.initialize();

        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}