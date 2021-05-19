package com.ball.auth.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_DEFAULT)
public class UserPatchModel {

    private String email;
    private Map<String, String> meta;

}
