package com.hcmus.fastfood.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.fastfood.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

}
