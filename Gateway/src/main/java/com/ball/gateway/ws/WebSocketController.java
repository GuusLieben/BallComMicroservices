package com.ball.gateway.ws;

import com.ball.gateway.AuthenticationFilter;
import com.ball.gateway.config.filters.GatewayConfiguration;
import com.ball.gateway.config.ws.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@CrossOrigin(originPatterns = "**", allowCredentials = "false")
@Controller
public class WebSocketController {

    private final GatewayConfiguration configuration;
    private final RestTemplateBuilder templateBuilder;
    private final ObjectMapper mapper = new ObjectMapper();

    public WebSocketController(GatewayConfiguration configuration, RestTemplateBuilder builder) {
        this.configuration = configuration;
        this.templateBuilder = builder;
    }

    @MessageMapping("/chat.register/{topicId}")
    @SendTo("/topic/{topicId}")
    public SocketMessage register(@DestinationVariable String topicId, @Payload SocketMessage socketMessage, SimpMessageHeaderAccessor headerAccessor) throws IOException {
        return this.updateHandlers(topicId, socketMessage, headerAccessor);
    }

    @MessageMapping("/chat.send/{topicId}")
    @SendTo("/topic/{topicId}")
    public SocketMessage sendMessage(@DestinationVariable String topicId, @Payload SocketMessage socketMessage, SimpMessageHeaderAccessor headerAccessor) throws IOException {
        return this.updateHandlers(topicId, socketMessage, headerAccessor);
    }

    private SocketMessage updateHandlers(String topicId, SocketMessage message, SimpMessageHeaderAccessor headerAccessor) throws IOException {
        Handler active = null;
        for (Handler handler : this.configuration.getWs().getHandlers()) {
            if (AuthenticationFilter.wildcardMatch(topicId, handler.getPrefix())) {
                active = handler;
                break;
            }
        }

        if (active == null) return message;

        RestTemplate template = this.templateBuilder.build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headers.set(HttpHeaders.CONNECTION, "keep-alive");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = template.postForEntity(active.getUrl(), message, String.class);
        if (responseEntity != null) {
            message = this.mapper.readValue(responseEntity.getBody(), SocketMessage.class);
            headerAccessor.getSessionAttributes().putAll(message.getMeta());
        }

        return message;
    }
}
