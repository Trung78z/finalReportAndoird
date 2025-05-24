package com.hcmus.fastfood.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.fastfood.model.Transaction;
import com.hcmus.fastfood.service.TransactionService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Extracted from JWT access token
        Transaction saved = transactionService.save(transaction, username);
        return ResponseEntityUtils.created(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntityUtils.success(transactions);
    }
}