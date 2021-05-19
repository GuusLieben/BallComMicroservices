package com.ball.names;

import com.ball.names.collector.NameCollector;
import com.ball.names.collector.randomuser.RandomUserAPICollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class BallComNames {

    public static void main(String[] args) {
        SpringApplication.run(BallComNames.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("names");
    }

    @Bean
    public NameCollector collector(RestTemplateBuilder builder) {
        return new RandomUserAPICollector(builder);
    }

}
