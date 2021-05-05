package nl.avans.infrastructure.broker;

import nl.avans.infrastructure.broker.events.listen.ListenEvent;
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
    public void listener(Message message, ListenEvent listenEvent) {
        String messageType = message.getMessageProperties().getHeader("MessageType");
        System.out.println("Received event: " + messageType + " (" + listenEvent + ")");
    }
}
