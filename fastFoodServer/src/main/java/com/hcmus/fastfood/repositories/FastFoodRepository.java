package com.hcmus.fastfood.repositories;

import com.hcmus.fastfood.model.FastFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FastFoodRepository extends JpaRepository<FastFood, String> {
}