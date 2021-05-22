package nl.avans.infrastructure.broker.events;

import java.io.Serializable;

public interface ListenEvent extends Serializable {
    void execute();
}

