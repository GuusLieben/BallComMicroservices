package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.models.amqp.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public enum MessageHandler {
    EVENT_HANDLER(AuthEvent.class, AuthEventHandler.class, AuthEventHandler::handle,
            "CustomerAdded", "EmployeeAdded", "CustomerUpdated", "EmployeeUpdated", "CustomerDeleted", "EmployeeDeleted"),
    USERNAME_CREATED_HANDLER(AuthEvent.class, UserNameHandler.class, UserNameHandler::created,
            "CustomerAdded", "EmployeeAdded"),
    USERNAME_UPDATED_HANDLER(AuthEvent.class, UserNameHandler.class, UserNameHandler::updated,
            "CustomerUpdated", "EmployeeUpdated"),
    USERNAME_DELETED_HANDLER(AuthEvent.class, UserNameHandler.class, UserNameHandler::deleted,
            "CustomerDeleted", "EmployeeDeleted"),
    ;

    @Autowired
    private BeanFactory beanFactory;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String[] headers;
    private final Class<? extends Event> eventType;
    private final Class<? extends Handler> handlerType;
    private final BiConsumer<Handler, Event> action;

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

    public static void handle(String header, String body) {
        for (MessageHandler handler : lookup(header)) {
            final Handler bean = handler.beanFactory.getBean(handler.handlerType);
            try {
                Event event = MAPPER.readValue(body, handler.eventType);
                handler.action.accept(bean, event);
            } catch (Throwable t) {
                System.out.println("Could not handle " + body);
            }
        }
    }
}
