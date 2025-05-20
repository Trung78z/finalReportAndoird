package com.hcmus.fastfood.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmus.fastfood.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

}