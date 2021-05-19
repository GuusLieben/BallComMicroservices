package com.ball.support.models;


import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SocketMessage {

    private String content;
    private String sender;
    private String topic;
    private MessageType type;
    private Map<String, Object> meta = new HashMap<>();

}
