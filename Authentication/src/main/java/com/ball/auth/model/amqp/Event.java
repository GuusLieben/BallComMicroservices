package com.ball.auth.model.amqp;

import com.ball.auth.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Getter;

@Getter
public abstract class Event implements Serializable {

    @JsonProperty
    private String name;

    @JsonProperty
    private User user;

    protected Event(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public static class Created extends Event {

        public Created(String name, User customer) {
            super(name, customer);
        }
    }

    public static class Updated extends Event {

        public Updated(String name, User customer) {
            super(name, customer);
        }
    }

    public static class Deleted extends Event {

        public Deleted(String name, User customer) {
            super(name, customer);
        }
    }
}
