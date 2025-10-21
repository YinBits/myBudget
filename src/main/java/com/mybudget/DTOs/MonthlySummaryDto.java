package com.mybudget.DTOs;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummaryDto {
    private String month;
    private BigDecimal income;
    private BigDecimal expense;
}
