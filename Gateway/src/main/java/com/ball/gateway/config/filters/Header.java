package com.ball.gateway.config.filters;

import lombok.Data;

@Data
public class Header {

    private String token;
    private String payload;

}
