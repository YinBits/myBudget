package com.mybudget.Service;

import com.mybudget.DTOs.FinancialReportDto;
import com.mybudget.DTOs.MonthlySummaryDto;
import com.mybudget.DTOs.TransactionReportDto;
import com.mybudget.Entity.Transaction;
import com.mybudget.Repository.TransactionRepository;
import com.mybudget.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialReportService {

    private final TransactionRepository transactionRepository;

    public FinancialReportDto generateReportForUser(Long userId, LocalDate startDate, LocalDate endDate, boolean includeAll) {
        List<Transaction> transactions = fetchTransactions(userId, startDate, endDate, includeAll);

        BigDecimal totalIncome = calculateTotal(transactions, TransactionType.INCOME);
        BigDecimal totalExpense = calculateTotal(transactions, TransactionType.EXPENSE);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        BigDecimal averageIncome = calculateAverage(transactions, TransactionType.INCOME, totalIncome);
        BigDecimal averageExpense = calculateAverage(transactions, TransactionType.EXPENSE, totalExpense);

        List<MonthlySummaryDto> monthlySummary = generateMonthlySummary(transactions);
        List<TransactionReportDto> transactionDtos = mapToTransactionDtos(transactions);

        return FinancialReportDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .averageIncome(averageIncome)
                .averageExpense(averageExpense)
                .summaryByMonth(monthlySummary)
                .transactions(includeAll ? transactionDtos : null)
                .build();
    }

    // ----------------- Métodos privados -----------------

    private List<Transaction> fetchTransactions(Long userId, LocalDate startDate, LocalDate endDate, boolean includeAll) {
        return includeAll
                ? transactionRepository.findByUserId(userId)
                : transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    private BigDecimal calculateTotal(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAverage(List<Transaction> transactions, TransactionType type, BigDecimal total) {
        long count = transactions.stream().filter(t -> t.getType() == type).count();
        return count > 0
                ? total.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }

    private List<MonthlySummaryDto> generateMonthlySummary(List<Transaction> transactions) {
        Map<YearMonth, List<Transaction>> groupedByMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate())));

        return groupedByMonth.entrySet().stream()
                .map(entry -> MonthlySummaryDto.builder()
                        .month(entry.getKey().toString())
                        .income(calculateTotal(entry.getValue(), TransactionType.INCOME))
                        .expense(calculateTotal(entry.getValue(), TransactionType.EXPENSE))
                        .build())
                .sorted(Comparator.comparing(MonthlySummaryDto::getMonth))
                .collect(Collectors.toList());
    }

    private List<TransactionReportDto> mapToTransactionDtos(List<Transaction> transactions) {
        return transactions.stream()
                .map(t -> new TransactionReportDto(
                        t.getId(),
                        t.getDescription(),
                        t.getAmount(),
                        t.getType(),
                        t.getDate()
                ))
                .collect(Collectors.toList());
    }
}
