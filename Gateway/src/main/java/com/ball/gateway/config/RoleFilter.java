package com.ball.gateway.config;

import lombok.Data;

@Data
public class RoleFilter {

    private String path;
    private String method = "GET";
    private Role[] roles;

}
