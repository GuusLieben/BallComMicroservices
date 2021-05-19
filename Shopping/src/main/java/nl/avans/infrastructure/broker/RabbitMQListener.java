package nl.avans.infrastructure.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.avans.infrastructure.broker.events.listen.ListenEvent;
import nl.avans.infrastructure.broker.events.listen.ListenEventFactory;
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
            exchange = @Exchange("${ball.rabbitmq.exchange}"),
            key = "${ball.rabbitmq.queue}"
    ))
    public void listen(Message message, String listenEvent) throws JsonProcessingException {
        System.out.println("Yoo");
        String messageType = message.getMessageProperties().getHeader("MessageType");
        System.out.println("Received event: " + messageType + " (" + listenEvent + ")");
        ObjectMapper mapper = new ObjectMapper();
        try {
            ListenEvent lister = mapper.readValue(listenEvent, ListenEvent.class);
            ListenEventFactory.execute(messageType, lister);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }
}
