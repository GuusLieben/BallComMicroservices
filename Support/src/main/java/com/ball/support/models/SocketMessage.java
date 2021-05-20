package com.ball.support.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SocketMessage {

    private String content;
    private String sender;
    private String topic;
    private MessageType type;
    private Map<String, Object> meta = new HashMap<>();

}
