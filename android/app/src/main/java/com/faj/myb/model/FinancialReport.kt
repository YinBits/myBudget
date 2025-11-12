package com.faj.myb.model

import com.google.gson.annotations.SerializedName

data class FinancialReport(
    @SerializedName("totalIncome") val totalIncome: Double,
    @SerializedName("totalExpense") val totalExpense: Double,
    @SerializedName("balance") val balance: Double,
    @SerializedName("averageIncome") val averageIncome: Double,
    @SerializedName("averageExpense") val averageExpense: Double,
    @SerializedName("summaryByMonth") val summaryByMonth: List<MonthlySummary>,
    @SerializedName("transactions") val transactions: List<TransactionReport>
)
