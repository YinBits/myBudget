package com.mybudget.DTOs;

import com.mybudget.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionDto(
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDate date
) {
}