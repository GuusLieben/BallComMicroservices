package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.models.amqp.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum ListenableEvent {
    CUSTOMER_ADDED("CustomerAdded", AuthEvent.class, EventHandler::handle),
    EMPLOYEE_ADDED("EmployeeAdded", AuthEvent.class, EventHandler::handle),
    ;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String header;
    private final Class<? extends Event> eventType;
    private final BiConsumer<EventHandler, ? extends Event> action;

    <T extends Event> ListenableEvent(String header, Class<T> eventType, BiConsumer<EventHandler, T> action) {
        this.header = header;
        this.eventType = eventType;
        this.action = action;
    }

    <T extends Event> ListenableEvent(String header, Class<T> eventType, Consumer<T> action) {
        this.header = header;
        this.eventType = eventType;
        this.action = (EventHandler handler, T event) -> action.accept(event);
    }

    public static ListenableEvent lookup(String header) {
        for (ListenableEvent event : ListenableEvent.values()) {
            if (event.header.equals(header)) return event;
        }
        return null;
    }

    public void handle(String body, EventHandler handler) {
        try {
            Event event = MAPPER.readValue(body, this.eventType);
            //noinspection unchecked
            ((BiConsumer<EventHandler, Event>) this.action).accept(handler, event);
        } catch (Throwable t) {
            System.out.println("Could not handle " + body);
        }
    }
}
