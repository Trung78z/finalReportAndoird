package com.hcmus.fastfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.TransactionDTO;
import com.hcmus.fastfood.dto.TransactionItemDTO;
import com.hcmus.fastfood.model.Cart;
import com.hcmus.fastfood.model.Transaction;
import com.hcmus.fastfood.model.TransactionItem;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.CartRepository;
import com.hcmus.fastfood.repositories.TransactionRepository;
import com.hcmus.fastfood.repositories.UserRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public Transaction save(TransactionDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
System.out.println(dto);
        Transaction transaction = new Transaction();
        transaction.setTotalPrice(dto.getTotalPrice());
        transaction.setUser(user);

        for (TransactionItemDTO itemDTO : dto.getTransactionItems()) {
            Cart cart = cartRepository.findById(itemDTO.getCartId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            cart.setActive(false);
            cartRepository.save(cart);

            TransactionItem item = TransactionItem.builder()
                    .cart(cart)
                    .transaction(transaction)
                    .build();

            transaction.getTransactionItems().add(item);
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}