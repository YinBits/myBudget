package com.mybudget.DTOs;

import java.math.BigDecimal;

public record TransactionSummaryDto(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance
) {
}