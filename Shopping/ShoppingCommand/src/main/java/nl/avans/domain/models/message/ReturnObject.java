package nl.avans.domain.models.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnObject<T> {
    private String error;
    private T object;
}
