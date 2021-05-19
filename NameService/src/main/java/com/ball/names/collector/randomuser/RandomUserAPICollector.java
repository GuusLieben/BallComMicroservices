package com.ball.names.collector.randomuser;

import com.ball.names.collector.NameCollector;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RandomUserAPICollector implements NameCollector {

    private final RestTemplateBuilder templateBuilder;

    public RandomUserAPICollector(RestTemplateBuilder templateBuilder) {
        this.templateBuilder = templateBuilder;
    }

    @Override
    public List<String> collect() {
        RestTemplate template = this.templateBuilder.build();
        ResponseEntity<RandomUserList> responseEntity = template.getForEntity("https://randomuser.me/api/?results=100&inc=name&nat=us", RandomUserList.class);

        List<String> names = new ArrayList<>();
        for (RandomUserModel user : responseEntity.getBody().getResults()) {
            RandomUserName name = user.getName();
            names.add(name.getFirst() + ' ' + name.getLast());
        }

        return names;
    }
}
