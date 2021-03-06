package nl.avans.infrastructure.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQListener {
    private final ListenEventFactory listenEventFactory;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "true"),
            exchange = @Exchange(value = "${ball.rabbitmq.exchange}", type = ExchangeTypes.FANOUT)
    ))
    public void listen(Message message, String listenEvent) {
        String messageType = message.getMessageProperties().getHeader("MessageType");
        System.out.println("Received event: " + messageType + " (" + listenEvent + ")");
        try {
            listenEventFactory.execute(messageType, listenEvent);
        } catch (Exception e) {
            System.out.println("Execute " + messageType + "event failed");
        }
    }
}
