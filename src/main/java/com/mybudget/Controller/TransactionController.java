package com.mybudget.Controller;

import com.mybudget.DTOs.*;
import com.mybudget.Service.TransactionService;
import com.mybudget.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //Dashboard
    @GetMapping("/dashboard")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<TransactionSummaryDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        TransactionSummaryDto summary = transactionService.getMonthlySummary(year, month);
        return ResponseEntity.ok(summary);
    }


    // CREATE
    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionDto createTransactionDto) {
        TransactionResponseDto response = transactionService.createTransaction(createTransactionDto);
        return ResponseEntity.ok(response);
    }

    // READ - Todas as transações
    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        List<TransactionResponseDto> transactions = transactionService.getUserTransactions();
        return ResponseEntity.ok(transactions);
    }

    // READ - Transação por ID
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    // READ - Transações por tipo
    @GetMapping("/type/{type}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(@PathVariable TransactionType type) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    // READ - Transações por período
    @GetMapping("/period")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByPeriod(start, end);
        return ResponseEntity.ok(transactions);
    }

    // UPDATE
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody UpdateTransactionDto updateTransactionDto) {
        TransactionResponseDto response = transactionService.updateTransaction(id, updateTransactionDto);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}