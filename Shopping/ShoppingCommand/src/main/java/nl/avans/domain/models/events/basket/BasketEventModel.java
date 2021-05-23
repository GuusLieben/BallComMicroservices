package nl.avans.domain.models.events.basket;

import lombok.Data;

import java.text.DateFormat;
import java.util.UUID;

@Data
public class BasketEventModel {
    private UUID customerId;
    private DateFormat date;
    private String event;
    private String data;
}
