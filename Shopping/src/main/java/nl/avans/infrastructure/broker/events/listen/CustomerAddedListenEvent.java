package nl.avans.infrastructure.broker.events.listen;

import nl.avans.domain.models.Product;

public class CustomerAddedListenEvent extends ListenEvent {
    protected CustomerAddedListenEvent(Product product) {
        super("CustomerAdded");
    }
}
