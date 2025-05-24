package com.hcmus.fastfood.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcmus.fastfood.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findById(String userId);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRole(@Param("username") String username);

}