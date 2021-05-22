package com.ball.auth.model.amqp;

import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Event implements Serializable {

    private UUID guid;
    private Map<String, String> meta;
    private String email;
    private UserRole role;

    protected Event(User user) {
        this.guid = user.getGuid();
        this.meta = user.getMeta();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public static class Created extends Event {

        public Created(User customer) {
            super(customer);
        }
    }

    public static class Updated extends Event {

        public Updated(User customer) {
            super(customer);
        }
    }

    public static class Deleted extends Event {

        public Deleted(User customer) {
            super(customer);
        }
    }
}
