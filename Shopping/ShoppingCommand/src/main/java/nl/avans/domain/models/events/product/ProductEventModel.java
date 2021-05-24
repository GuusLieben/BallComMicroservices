package nl.avans.domain.models.events.product;

import java.text.DateFormat;
import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@ToString
public class ProductEventModel {
    private UUID productId;
    private DateFormat date;
    private String event;
    private String data;
}

