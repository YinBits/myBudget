package com.mybudget.DTOs;

import com.mybudget.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDate date,
        BigDecimal currentBalance
) {
}