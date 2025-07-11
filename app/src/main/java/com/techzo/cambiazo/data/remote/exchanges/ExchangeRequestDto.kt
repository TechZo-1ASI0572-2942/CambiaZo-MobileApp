package com.techzo.cambiazo.data.remote.exchanges

import com.google.gson.annotations.SerializedName

data class ExchangeRequestDto(
    @SerializedName("productOwnId")
    val productOwnId: Int,

    @SerializedName("productChangeId")
    val productChangeId: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("locationId")
    val locationId: Int
)

fun ExchangeRequestDto.toExchangeRequestDto(): ExchangeRequestDto {
    return ExchangeRequestDto(
        productOwnId = productOwnId,
        productChangeId = productChangeId,
        status = status,
        locationId = locationId
    )
}
