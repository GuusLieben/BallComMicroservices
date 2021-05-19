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
    private final BiConsumer<EventHandler, ? extends Event>[] actions;

    @SafeVarargs
    <T extends Event> ListenableEvent(String header, Class<T> eventType, BiConsumer<EventHandler, T>... actions) {
        this.header = header;
        this.eventType = eventType;
        this.actions = actions;
    }

    <T extends Event> ListenableEvent(String header, Class<T> eventType, Consumer<T> actions) {
        this(header, eventType,  (EventHandler handler, T event) -> actions.accept(event));
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
            for (BiConsumer<EventHandler, Event> action : (BiConsumer<EventHandler, Event>[]) this.actions) {
                action.accept(handler, event);
            }
        } catch (Throwable t) {
            System.out.println("Could not handle " + body);
        }
    }
}
