package com.hcmus.fastfood.repository;

import com.hcmus.fastfood.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.food WHERE c.user.username = :username")
    List<Cart> findByUserNameWithFood(@Param("username") String username);
}