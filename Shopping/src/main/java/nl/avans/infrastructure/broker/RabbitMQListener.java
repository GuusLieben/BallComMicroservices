package nl.avans.infrastructure.broker;

import nl.avans.infrastructure.broker.events.listen.command.ListenEventCommandFactory;
import nl.avans.infrastructure.broker.events.listen.query.ListenEventQueryFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "true"),
            exchange = @Exchange(value = "${ball.rabbitmq.exchange}", type = ExchangeTypes.FANOUT)
    ))
    public void listen(Message message, String listenEvent) {
        String messageType = message.getMessageProperties().getHeader("MessageType");
        System.out.println("Received event: " + messageType + " (" + listenEvent + ")");
        ListenEventCommandFactory.execute(messageType, listenEvent);
        ListenEventQueryFactory.execute(messageType, listenEvent);
    }
}
