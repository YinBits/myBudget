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
import java.time.LocalDateTime;
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
                .date(createTransactionDto.date() != null ? createTransactionDto.date() : LocalDateTime.now())
                .user(user)
                .build();

        updateUserBudget(user, createTransactionDto.type(), createTransactionDto.amount(), true);
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return mapToResponseDto(savedTransaction, user.getBudget());
    }

    // READ - Buscar todas as transações do usuário
    public List<TransactionResponseDto> getUserTransactions() {
        User user = getAuthenticatedUser();
        List<Transaction> transactions = transactionRepository.findByUserOrderByDateDesc(user);

        return transactions.stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // READ - Buscar transação por ID
    public TransactionResponseDto getTransactionById(Long id) {
        User user = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        return mapToResponseDto(transaction, user.getBudget());
    }

    // READ - Buscar transações por tipo
    public List<TransactionResponseDto> getTransactionsByType(TransactionType type) {
        User user = getAuthenticatedUser();
        List<Transaction> transactions = transactionRepository.findByUserAndTypeOrderByDateDesc(user, type);

        return transactions.stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // READ - Buscar transações por período
    public List<TransactionResponseDto> getTransactionsByPeriod(LocalDateTime start, LocalDateTime end) {
        User user = getAuthenticatedUser();
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end);

        return transactions.stream()
                .map(transaction -> mapToResponseDto(transaction, user.getBudget()))
                .collect(Collectors.toList());
    }

    // UPDATE
    @Transactional
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionDto updateTransactionDto) {
        User user = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        // Reverter o valor antigo do orçamento
        updateUserBudget(user, transaction.getType(), transaction.getAmount(), false);

        // Atualizar campos
        if (updateTransactionDto.description() != null) {
            transaction.setDescription(updateTransactionDto.description());
        }
        if (updateTransactionDto.amount() != null) {
            transaction.setAmount(updateTransactionDto.amount());
        }
        if (updateTransactionDto.type() != null) {
            transaction.setType(updateTransactionDto.type());
        }
        if (updateTransactionDto.date() != null) {
            transaction.setDate(updateTransactionDto.date());
        }

        // Aplicar o novo valor ao orçamento
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

        // Reverter o valor do orçamento
        updateUserBudget(user, transaction.getType(), transaction.getAmount(), false);

        transactionRepository.delete(transaction);
        userRepository.save(user);
    }

    // Métodos auxiliares
    private User getAuthenticatedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void updateUserBudget(User user, TransactionType type, BigDecimal amount, boolean isAdd) {
        BigDecimal newBudget;

        if (type == TransactionType.INCOME) {
            newBudget = isAdd ?
                    user.getBudget().add(amount) :
                    user.getBudget().subtract(amount);
        } else { // EXPENSE
            newBudget = isAdd ?
                    user.getBudget().subtract(amount) :
                    user.getBudget().add(amount);
        }

        // Validar saldo para despesas
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

    // Relatórios
    public TransactionSummaryDto getMonthlySummary(int year, int month) {
        User user = getAuthenticatedUser();

        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);

        BigDecimal totalIncome = transactionRepository.sumAmountByUserAndTypeAndDateBetween(
                user, TransactionType.INCOME, start, end);
        BigDecimal totalExpenses = transactionRepository.sumAmountByUserAndTypeAndDateBetween(
                user, TransactionType.EXPENSE, start, end);
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new TransactionSummaryDto(totalIncome, totalExpenses, balance);
    }
}