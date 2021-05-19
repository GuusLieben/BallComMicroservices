package com.ball.auth.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginModel {

    private String email;
    private String password;

}
