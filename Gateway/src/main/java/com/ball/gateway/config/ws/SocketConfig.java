package com.ball.gateway.config.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.Data;

@Data
@Component
@Primary
@ConfigurationProperties(prefix = "gateway.ws")
public class SocketConfig {

    private List<Handler> handlers;

}
