package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.amqp.ShipmentRegisteredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum ListenableEvent {
    SHIPMENT_REGISERED("ShipmentRegistered", ShipmentRegisteredEvent.class, EventHandler::handle);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final EventHandler HANDLER = new EventHandler();
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

    public void handle(String body) {
        try {
            Event event = MAPPER.readValue(body, this.eventType);
            //noinspection unchecked
            ((BiConsumer<EventHandler, Event>) this.action).accept(HANDLER, event);
        } catch (Throwable t) {
            System.out.println("Could not handle " + body);
        }
    }
}
