package com.hcmus.fastfood.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.fastfood.model.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByName(String name);
}
