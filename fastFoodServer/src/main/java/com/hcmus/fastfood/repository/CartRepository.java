package com.hcmus.fastfood.repository;

import com.hcmus.fastfood.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
}