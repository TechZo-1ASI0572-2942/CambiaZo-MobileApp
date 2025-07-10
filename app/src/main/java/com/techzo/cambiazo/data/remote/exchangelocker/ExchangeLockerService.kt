package com.techzo.cambiazo.data.remote.exchangelocker

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExchangeLockerService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @GET("exchange-lockers/exchange/{exchangeId}/{userId}")
    suspend fun getExchangeLockerByExchangeIdAndUserId(
        @Path("exchangeId") exchangeId: Int,
        @Path("userId") userId: Int
    ): Response<ExchangeLockerDto>
}