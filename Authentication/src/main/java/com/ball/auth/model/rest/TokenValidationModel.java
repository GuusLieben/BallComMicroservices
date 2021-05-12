package com.ball.auth.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidationModel {

    private String token;
    private boolean valid;

}
