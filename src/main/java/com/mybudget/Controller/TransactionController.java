package com.mybudget.Controller;

import com.mybudget.DTOs.*;
import com.mybudget.Service.TransactionService;
import com.mybudget.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // CREATE
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionDto createTransactionDto) {
        TransactionResponseDto response = transactionService.createTransaction(createTransactionDto);
        return ResponseEntity.ok(response);
    }

    // READ - Todas as transações
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        List<TransactionResponseDto> transactions = transactionService.getUserTransactions();
        return ResponseEntity.ok(transactions);
    }

    // READ - Transação por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    // READ - Transações por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(@PathVariable TransactionType type) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    // READ - Transações por período
    @GetMapping("/period")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByPeriod(start, end);
        return ResponseEntity.ok(transactions);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody UpdateTransactionDto updateTransactionDto) {
        TransactionResponseDto response = transactionService.updateTransaction(id, updateTransactionDto);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // RELATÓRIOS - Resumo mensal
    @GetMapping("/summary/monthly")
    public ResponseEntity<TransactionSummaryDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        TransactionSummaryDto summary = transactionService.getMonthlySummary(year, month);
        return ResponseEntity.ok(summary);
    }
}