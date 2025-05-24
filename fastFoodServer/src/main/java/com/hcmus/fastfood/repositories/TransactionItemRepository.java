package com.hcmus.fastfood.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.fastfood.model.TransactionItem;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Integer> {

}