package com.mybudget.DTOs;

import com.mybudget.Entity.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class FinancialReportDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private BigDecimal averageIncome;
    private BigDecimal averageExpense;
    private List<MonthlySummaryDto> summaryByMonth;
    private List<TransactionReportDto> transactions;
}
