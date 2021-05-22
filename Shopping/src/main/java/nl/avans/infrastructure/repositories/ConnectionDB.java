package nl.avans.infrastructure.repositories;

import java.sql.Connection;

public interface ConnectionDB {
    Connection connect();
}
