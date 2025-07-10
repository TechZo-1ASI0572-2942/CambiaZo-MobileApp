package com.techzo.cambiazo.data.remote.lockerlocation

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface LockerLocationService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @GET("locations")
    suspend fun getLockerLocations(): Response<List<LockerLocationDto>>

    @GET("locations/{id}")
    suspend fun getLockerLocationById(@Path("id") id: Int): Response<LockerLocationDto>
}