package com.ball.support.models.amqp;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthEvent implements Event {

    private UUID guid;
    private Map<String, String> meta;
    private String email;
    private String role;

}
