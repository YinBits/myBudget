package com.faj.myb.api.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class DashboardResponse(
    @SerializedName("balance") val balance: BigInteger
)
