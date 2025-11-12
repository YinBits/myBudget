package com.faj.myb.model

import com.google.gson.annotations.SerializedName

data class MonthlySummary(
    @SerializedName("month") val month: String,
    @SerializedName("income") val income: Double,
    @SerializedName("expense") val expense: Double
)
