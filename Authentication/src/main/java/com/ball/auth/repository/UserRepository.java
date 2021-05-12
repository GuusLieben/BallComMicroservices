package com.ball.auth.repository;

import com.ball.auth.model.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailEquals(String email);

}

