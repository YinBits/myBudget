package com.faj.myb.api

import com.faj.myb.api.request.LoginRequest
import com.faj.myb.api.request.SignUpRequest
import com.faj.myb.api.request.TransactionRequest
import com.faj.myb.api.response.LoginResponse
import com.faj.myb.api.response.TransactionResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("users")
    suspend fun register(@Body request: SignUpRequest)

    @POST("transactions")
    suspend fun addTransaction(@Body request: TransactionRequest)

    @GET("transactions")
    suspend fun getTransactions(): List<TransactionResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3001/"
    var token: String? = null

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            it.proceed(request.build())
        }
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
