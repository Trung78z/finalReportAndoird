package com.hcmus.fastfood.repositories;

import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.FastFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c.food FROM Cart c WHERE c.user.username = :username")
    List<FastFood> findFoodsByUserName(@Param("username") String username);
}