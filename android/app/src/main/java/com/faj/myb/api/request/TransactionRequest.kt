package com.faj.myb.api.request

import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("type") val type: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String
)
