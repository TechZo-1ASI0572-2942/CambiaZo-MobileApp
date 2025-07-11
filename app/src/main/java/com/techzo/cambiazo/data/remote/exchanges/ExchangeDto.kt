package com.techzo.cambiazo.data.remote.exchanges

import com.google.gson.annotations.SerializedName
import com.techzo.cambiazo.data.repository.ProductRepository
import com.techzo.cambiazo.data.repository.UserRepository
import com.techzo.cambiazo.domain.Exchange
import com.techzo.cambiazo.domain.ExchangeProduct
import com.techzo.cambiazo.domain.LocationLocker
import com.techzo.cambiazo.domain.User

data class ExchangeDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productOwn")
    val productOwn: ExchangeProduct,
    @SerializedName("productChange")
    val productChange: ExchangeProduct,
    @SerializedName("userOwn")
    val userOwn: User,
    @SerializedName("userChange")
    val userChange: User,
    @SerializedName("status")
    val status: String,
    @SerializedName("exchangeDate")
    val exchangeDate: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("location")
    val location: LocationLocker?,

)

fun ExchangeDto.toExchange(): Exchange {
    return Exchange(
        id = id,
        productOwn = productOwn,
        productChange = productChange,
        userOwn = userOwn,
        userChange = userChange,
        status = status,
        exchangeDate = exchangeDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        location = location
    )
}
