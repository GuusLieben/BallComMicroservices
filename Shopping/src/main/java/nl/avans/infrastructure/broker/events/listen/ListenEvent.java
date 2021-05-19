package nl.avans.infrastructure.broker.events.listen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ListenEvent implements Serializable {

    @JsonProperty
    private String name;

    protected ListenEvent(String name) {
        this.name = name;
    }
}

