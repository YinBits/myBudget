package com.faj.myb.model

import com.google.gson.annotations.SerializedName

data class TransactionReport(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val type: TransactionType,
    @SerializedName("date") val date: String
)
