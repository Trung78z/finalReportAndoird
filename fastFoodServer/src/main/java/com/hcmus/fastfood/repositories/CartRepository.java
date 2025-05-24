package com.hcmus.fastfood.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.FastFood;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c.food FROM Cart c WHERE c.user.username = :username")
    List<FastFood> findFoodsByUserName(@Param("username") String username);

    @Query("SELECT c FROM Cart c JOIN FETCH c.food WHERE c.user.username = :username AND c.isActive = true")
    List<Cart> findCartsWithFoodByUserName(@Param("username") String username);
}