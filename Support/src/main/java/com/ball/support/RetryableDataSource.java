package com.ball.support;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RetryableDataSource extends AbstractDataSource {

    private final DataSource dataSource;

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 10_000))
    public Connection getConnection() throws SQLException {
        log.info("Opening connection ...");
        return this.dataSource.getConnection();
    }

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 10_000))
    public Connection getConnection(String username, String password) throws SQLException {
        log.info("Opening connection ...");
        return this.dataSource.getConnection(username, password);
    }
}
