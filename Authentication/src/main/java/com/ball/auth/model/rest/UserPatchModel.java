package com.ball.auth.model.rest;

import com.ball.auth.model.UserMeta;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_DEFAULT)
public class UserPatchModel {

    private String email;
    private List<UserMeta> meta;

}
