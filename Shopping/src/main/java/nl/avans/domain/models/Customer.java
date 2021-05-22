package nl.avans.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Customer {
    private UUID customerId;
    private String event;
}
