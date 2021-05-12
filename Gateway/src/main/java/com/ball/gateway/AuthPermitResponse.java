package com.ball.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Map;

import lombok.Data;

@Data
public class AuthPermitResponse {

    private boolean permitted;
    @JsonInclude(Include.NON_NULL)
    private Map<String, Object> payload;

}
