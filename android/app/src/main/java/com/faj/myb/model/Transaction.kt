package com.faj.myb.model

import java.util.Date

data class Transaction(
    val id: Long,
    val name: String,
    val date: Date,
    val value: Double,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE
}