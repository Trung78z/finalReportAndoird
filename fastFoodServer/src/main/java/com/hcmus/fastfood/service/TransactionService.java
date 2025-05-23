package com.hcmus.fastfood.service;

import com.hcmus.fastfood.model.Transaction;
import com.hcmus.fastfood.repositories.CartRepository;
import com.hcmus.fastfood.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
        @Autowired
    private CartRepository cartRepository;

    public Transaction save(Transaction transaction) {
        // Set the cart to inactive flow userName
        cartRepository.findCartsWithFoodByUserName(transaction.getUser().getUsername()).forEach(cart -> {
            cart.setActive(false);
            cartRepository.save(cart);
        });
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}