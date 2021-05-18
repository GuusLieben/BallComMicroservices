package com.ball.gateway.config.filters;

import lombok.Data;

@Data
public class RoleFilter {

    private String path;
    private String method = "ALL";
    private Role[] roles;

}
