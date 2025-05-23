package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.model.Transaction;
import com.hcmus.fastfood.service.TransactionService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.save(transaction);
        return ResponseEntityUtils.created(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntityUtils.success(transactions);
    }
}