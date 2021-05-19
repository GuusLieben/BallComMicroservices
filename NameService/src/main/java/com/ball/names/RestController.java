package com.ball.names;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CacheController controller;
    private final Random random = new Random();

    @GetMapping("/")
    public List<String> getAll() {
        return this.controller.findLatestNames();
    }

    @GetMapping("/single")
    public String getSingle() {
        int index = this.random.nextInt(100) + 1;
        return this.controller.findLatestNames().get(index);
    }
}
