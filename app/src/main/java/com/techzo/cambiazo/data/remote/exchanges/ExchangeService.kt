package com.techzo.cambiazo.data.remote.exchanges

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExchangeService {

    @Headers("Content-Type: application/json",
        "Accept: application/json")
    @GET("exchanges/userOwn/{userId}")
    suspend fun getExchangesByUserOwnId(@Path("userId") userId: Int): Response<List<ExchangeDto>>

    @Headers("Content-Type: application/json",
        "Accept: application/json")
    @GET("exchanges/userChange/{userId}")
    suspend fun getExchangesByUserChangeId(@Path("userId") userId: Int): Response<List<ExchangeDto>>

}