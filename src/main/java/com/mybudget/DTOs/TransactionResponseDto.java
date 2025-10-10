package com.mybudget.DTOs;

import com.mybudget.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDateTime date,
        BigDecimal currentBalance
) {
}