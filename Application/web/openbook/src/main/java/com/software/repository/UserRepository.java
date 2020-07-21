package com.software.repository;

import com.software.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT b FROM User b LEFT JOIN FETCH b.like WHERE b.email = :email")
    Optional<User> findCompleteUser(@Param("email") String email);
}
