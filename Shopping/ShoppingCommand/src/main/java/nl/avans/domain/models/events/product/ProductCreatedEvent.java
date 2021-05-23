package nl.avans.domain.models.events.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.avans.domain.models.events.Event;
import nl.avans.domain.models.models.Product;

import java.util.UUID;

public class ProductCreatedEvent implements Event {
    @JsonProperty
    private UUID productId;

    @JsonProperty
    private String name;

    @JsonProperty
    private int amount;

    @JsonProperty
    private String description;

    @JsonProperty
    private double price;

    @JsonProperty
    private double weight;

    @JsonProperty
    private String brandName;

    @JsonProperty
    private String supplierName;
}
