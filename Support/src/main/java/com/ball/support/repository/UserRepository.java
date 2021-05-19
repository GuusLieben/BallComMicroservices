package com.ball.support.repository;

import com.ball.support.models.rest.UserName;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserName, String> {
}
