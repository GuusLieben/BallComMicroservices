package nl.avans.infrastructure.repository;

import java.sql.Connection;

public interface ConnectionDB {
    Connection connect();
}
