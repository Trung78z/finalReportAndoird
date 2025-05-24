package com.hcmus.fastfood.service;

import com.hcmus.fastfood.model.Transaction;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.CartRepository;
import com.hcmus.fastfood.repositories.TransactionRepository;
import com.hcmus.fastfood.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public Transaction save(Transaction transaction, String username) {
        // Set the cart to inactive flow userName
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        transaction.setUser(user);

        cartRepository.findCartsWithFoodByUserName(user.getUsername()).forEach(cart -> {
            cart.setActive(false);
            cartRepository.save(cart);
        });
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}