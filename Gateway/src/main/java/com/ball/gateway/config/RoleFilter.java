package com.ball.gateway.config;

import lombok.Data;

@Data
public class RoleFilter {

    private String path;
    private Role[] roles;

}
