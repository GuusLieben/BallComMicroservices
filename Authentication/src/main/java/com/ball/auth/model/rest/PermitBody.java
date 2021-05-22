package com.ball.auth.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Map;

import lombok.Data;

@Data
public class PermitBody {

    private final boolean permitted;
    @JsonInclude(Include.NON_NULL)
    private final Map<String, Object> payload;

    public static PermitBody deny() {
        return new PermitBody(false, null);
    }

}
