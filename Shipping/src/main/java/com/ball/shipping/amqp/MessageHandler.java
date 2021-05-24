package com.ball.shipping.amqp;

import com.ball.shipping.model.amqp.Event;
import com.ball.shipping.model.amqp.order.OrderCreatedEvent;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public enum MessageHandler {
    ORDER_CREATED(OrderCreatedEvent.class, EventHandler.class, EventHandler::created,
            "OrderCreated"),
    ORDER_CREATED_EVENT(OrderCreatedEvent.class, OrderEventHandler.class, OrderEventHandler::handle,
            "OrderCreated"),
    ;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String[] headers;
    private final Class<? extends Event> eventType;
    private final Class<? extends Handler> handlerType;
    private final BiConsumer<Handler, Event> action;

    static {
        MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @SuppressWarnings("unchecked")
    <T extends Event, H extends Handler> MessageHandler(Class<T> eventType, Class<H> handlerType, BiConsumer<H, T> action, String... headers) {
        this.eventType = eventType;
        this.handlerType = handlerType;
        this.action = (BiConsumer<Handler, Event>) action;
        this.headers = headers;
    }

    public static List<MessageHandler> lookup(String header) {
        List<MessageHandler> events = new ArrayList<>();
        for (MessageHandler event : MessageHandler.values()) {
            if (Arrays.asList(event.headers).contains(header)) events.add(event);
        }
        return events;
    }

    public static void handle(String header, String body, BeanFactory beanFactory) {
        for (MessageHandler handler : lookup(header)) {
            final Handler bean = beanFactory.getBean(handler.handlerType);
            try {
                Event event = MAPPER.readValue(body, handler.eventType);
                handler.action.accept(bean, event);
            } catch (Throwable t) {
                System.out.println("Could not handle " + body);
            }
        }
    }
}
