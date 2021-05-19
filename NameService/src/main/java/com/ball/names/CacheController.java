package com.ball.names;

import com.ball.names.collector.NameCollector;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CacheController {

    private final NameCollector collector;

    public CacheController(NameCollector collector) {
        this.collector = collector;
    }

    @Cacheable("names")
    public List<String> findLatestNames() {
        return this.collector.collect();
    }

    // Every 5 minutes (defined in ms)
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @CacheEvict(value = "names", allEntries = true)
    public void evictAll() {}

}
