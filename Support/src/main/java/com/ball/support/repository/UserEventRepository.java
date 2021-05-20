package com.ball.support.repository;

import com.ball.support.models.amqp.AuthEvent;

import org.springframework.data.repository.CrudRepository;

public interface UserEventRepository extends CrudRepository<AuthEvent, String> {
}
