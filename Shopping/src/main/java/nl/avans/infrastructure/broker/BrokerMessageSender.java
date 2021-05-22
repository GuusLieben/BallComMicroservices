package nl.avans.infrastructure.broker;

import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Customer;
import nl.avans.domain.models.Order;

import java.util.UUID;

public interface BrokerMessageSender {
    void orderCreated(Order order);
    void basketItemAdded(BasketItem basketItem, UUID customerId);
    void basketItemRemoved(BasketItem basketItem, UUID customerId);
}
