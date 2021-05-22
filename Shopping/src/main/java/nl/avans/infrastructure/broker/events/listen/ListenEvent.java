package nl.avans.infrastructure.broker.events.listen;

import nl.avans.infrastructure.repositories.RepositoryAbstractFactory;

import java.io.Serializable;

public interface ListenEvent extends Serializable {
    void execute(RepositoryAbstractFactory repositoryAbstractFactory);
}

