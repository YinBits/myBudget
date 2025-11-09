package com.faj.myb.api.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TransactionResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("currentBalance") val currentBalance: BigDecimal
)
