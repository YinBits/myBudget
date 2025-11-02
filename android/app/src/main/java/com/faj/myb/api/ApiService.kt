package com.faj.myb.api

import com.faj.myb.api.request.LoginRequest
import com.faj.myb.api.response.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest) : Call<LoginResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "http://localhost:8080/"
    var token: String? = null
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}