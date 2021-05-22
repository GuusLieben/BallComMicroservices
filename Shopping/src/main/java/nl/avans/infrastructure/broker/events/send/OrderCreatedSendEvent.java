package nl.avans.infrastructure.broker.events.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.avans.domain.models.Basket;
import nl.avans.domain.models.BasketItem;
import nl.avans.domain.models.Order;
import nl.avans.domain.models.PaymentType;
import nl.avans.infrastructure.repositories.interfaces.BasketRepository;
import nl.avans.infrastructure.repositories.read_db.BasketRepositoryReadDBMSSQL;
import nl.avans.infrastructure.repositories.read_db.RepositoryReadDBFactory;

import java.util.ArrayList;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class OrderCreatedSendEvent implements SendEvent {
    @JsonProperty
    private UUID orderId;

    @JsonProperty
    private UUID customerId;

    @JsonProperty
    private ArrayList<BasketItem> basket;

    @JsonProperty
    private PaymentType paymentType;

    @JsonProperty
    private String street;

    @JsonProperty
    private String city;

    @JsonProperty
    private String postCode;

    @JsonProperty
    private int houseNumber;

    @JsonProperty
    private String additional;

    public OrderCreatedSendEvent(Order order) {
        this.orderId = UUID.randomUUID();
        this.customerId = order.getCustomerId();
        this.paymentType = order.getPaymentType();
        this.street = order.getStreet();
        this.city = order.getCity();
        this.postCode = order.getPostCode();
        this.houseNumber = order.getHouseNumber();
        this.additional = order.getAdditional();

        BasketRepository repository = new RepositoryReadDBFactory().createBasketRepository();
        this.basket = repository.get(order.getCustomerId()).getProducts();
    }
}
