package nl.avans;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Value("${ball.rabbitmq.queue}")
    private String queueKey;
    @Value("${ball.rabbitmq.exchange}")
    private String exchange;

//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(exchange);
//    }

    @Bean
    public Queue getShoppingQueue() {
        return new Queue(this.queueKey, true);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
