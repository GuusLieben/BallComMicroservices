package nl.avans.infrastructure.broker;

import lombok.RequiredArgsConstructor;
import nl.avans.infrastructure.broker.events.listen.BasketListener;
import nl.avans.infrastructure.broker.events.listen.ProductListener;
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
    private final ProductListener productListener;
    private final BasketListener basketListener;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ball.rabbitmq.queue}", durable = "true"),
            exchange = @Exchange(value = "${ball.rabbitmq.exchange}", type = ExchangeTypes.FANOUT)
    ))
    public void listen(Message message, String listenEvent) {
        String messageType = message.getMessageProperties().getHeader("MessageType");
        System.out.println("Received event: " + messageType + " (" + listenEvent + ")");
        productListener.execute(messageType, listenEvent);
        basketListener.execute(messageType, listenEvent);
    }
}
