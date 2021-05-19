package com.ball.auth.model.rest;

import com.ball.auth.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_DEFAULT)
public class UserCreateModel {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private UserRole role;

}
