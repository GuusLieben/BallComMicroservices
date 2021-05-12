package com.ball.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.Data;

@Data
@Component
@Primary
@ConfigurationProperties(prefix = "gateway")
public class GatewayConfiguration {

    private Header header;
    private Target target;
    private List<RoleFilter> filters;

}
