package com.ball.shipping.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "false"),
            exchange = @Exchange("${ball.rabbitmq.exchange}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void whenShipmentRegistered(Message message, String body) {
        Object messageType = message.getMessageProperties().getHeader("MessageType");
        ListenableEvent event = ListenableEvent.lookup(String.valueOf(messageType));
        if (event != null) event.handle(body);
    }
}
