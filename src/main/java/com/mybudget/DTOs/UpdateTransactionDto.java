package com.mybudget.DTOs;

import com.mybudget.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateTransactionDto(
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDateTime date
) {
}