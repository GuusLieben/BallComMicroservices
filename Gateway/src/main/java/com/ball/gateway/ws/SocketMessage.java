package com.ball.gateway.ws;


import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SocketMessage {

    private String content;
    private String sender;
    private MessageType type;
    private boolean anonymous;
    private Map<String, Object> meta = new HashMap<>();

}
