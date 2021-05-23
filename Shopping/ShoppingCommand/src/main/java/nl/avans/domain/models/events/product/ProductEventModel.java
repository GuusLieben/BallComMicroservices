package nl.avans.domain.models.events.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ProductEventModel {
    private UUID productId;
    private DateFormat date;
    private String event;
    private String data;
}
