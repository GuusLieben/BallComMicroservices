package com.ball.support.rest;

import com.ball.support.models.SocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class SupportController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplateBuilder templateBuilder;
    private final Map<String, String> nameStorage = new HashMap<>();

    public SupportController(RestTemplateBuilder templateBuilder) {
        this.templateBuilder = templateBuilder;
    }

    @PostMapping("chat")
    public SocketMessage chat(@RequestBody SocketMessage message) {
        String identifier = message.getTopic() + ':' + message.getSender();
        boolean anonymous = message.getMeta().containsKey("anonymous") && (boolean) message.getMeta().get("anonymous");

        switch (message.getType()) {
            case JOIN:
                if (anonymous) {
                        RestTemplate template = this.templateBuilder.build();
                        ResponseEntity<String> responseEntity = template.getForEntity("http://localhost:8088/single", String.class);
                        this.nameStorage.putIfAbsent(identifier, responseEntity.getBody());
                } else {
                    this.nameStorage.putIfAbsent(identifier, message.getSender());
                }
                break;
            case LEAVE:
            case CHAT:
            default:
                break;
        }

        if (anonymous) message.setSender(null);
        message.getMeta().put("name", this.name(identifier));

        return message;
    }

    private String name(String identifier) {
        return this.nameStorage.getOrDefault(identifier, "Anonymous");
    }
}
