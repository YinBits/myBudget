package com.mybudget.Service;

import com.mybudget.DTOs.*;
import com.mybudget.Entity.Transaction;
import com.mybudget.Entity.User;
import com.mybudget.Repository.TransactionRepository;
import com.mybudget.Repository.UserRepository;
import com.mybudget.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE
    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionDto createTransactionDto) {
        User user = getAuthenticatedUser();

        Transaction newTransaction = Transaction.builder()
                .type(createTransactionDto.type())
                .description(createTransactionDto.description())
                .amount(createTransactionDto.amount())
                .date(createTransactionDto.date() != null ? createTransactionDto.date() : LocalDate.now())
                .user(user)
                .build();

        updateUserBudget(user, createTransactionDto.type(), createTransactionDto.amount(), true);
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return mapToResponseDto(savedTransaction, user.getBudget());
    }

    // READ - Todas transações
    public List<TransactionResponseDto> getUserTransactions() {
        User user = getAuthenticatedUser();
        return transactionRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // READ - Por ID
    public TransactionResponseDto getTransactionById(Long id) {
        User user = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        return mapToResponseDto(transaction, user.getBudget());
    }

    // READ - Por tipo
    public List<TransactionResponseDto> getTransactionsByType(TransactionType type) {
        User user = getAuthenticatedUser();
        return transactionRepository.findByUserAndTypeOrderByDateDesc(user, type)
                .stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // READ - Por período
    public List<TransactionResponseDto> getTransactionsByPeriod(LocalDate start, LocalDate end) {
        User user = getAuthenticatedUser();
        return transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end)
                .stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // UPDATE
    @Transactional
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionDto updateTransactionDto) {
        User user = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        updateUserBudget(user, transaction.getType(), transaction.getAmount(), false);

        if (updateTransactionDto.description() != null)
            transaction.setDescription(updateTransactionDto.description());
        if (updateTransactionDto.amount() != null)
            transaction.setAmount(updateTransactionDto.amount());
        if (updateTransactionDto.type() != null)
            transaction.setType(updateTransactionDto.type());
        if (updateTransactionDto.date() != null)
            transaction.setDate(updateTransactionDto.date());

        updateUserBudget(user, transaction.getType(), transaction.getAmount(), true);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        userRepository.save(user);

        return mapToResponseDto(updatedTransaction, user.getBudget());
    }

    // DELETE
    @Transactional
    public void deleteTransaction(Long id) {
        User user = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        updateUserBudget(user, transaction.getType(), transaction.getAmount(), false);
        transactionRepository.delete(transaction);
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void updateUserBudget(User user, TransactionType type, BigDecimal amount, boolean isAdd) {
        BigDecimal newBudget;

        if (type == TransactionType.INCOME) {
            newBudget = isAdd ? user.getBudget().add(amount) : user.getBudget().subtract(amount);
        } else {
            newBudget = isAdd ? user.getBudget().subtract(amount) : user.getBudget().add(amount);
        }

        if (newBudget.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Saldo insuficiente para esta transação");
        }

        user.setBudget(newBudget);
    }

    private TransactionResponseDto mapToResponseDto(Transaction transaction, BigDecimal currentBalance) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                currentBalance
        );
    }

    // DASHBOARD - resumo mensal
    public TransactionSummaryDto getMonthlySummary(int year, int month) {
        User user = getAuthenticatedUser();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        BigDecimal totalIncome = transactionRepository.sumAmountByUserAndTypeAndDateBetween(
                user, TransactionType.INCOME, start, end);
        BigDecimal totalExpenses = transactionRepository.sumAmountByUserAndTypeAndDateBetween(
                user, TransactionType.EXPENSE, start, end);
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new TransactionSummaryDto(totalIncome, totalExpenses, balance);
    }
}
