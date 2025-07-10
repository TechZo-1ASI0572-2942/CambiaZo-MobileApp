package com.techzo.cambiazo.data.remote.exchangelocker

import com.techzo.cambiazo.domain.ExchangeLocation
import com.techzo.cambiazo.domain.ExchangeLocker

data class ExchangeLockerDto(
    val pinDeposit: String,
    val pinRetrieve: String,
    val lockerDepositId: String,
    val lockerRetrieveId: String,
    val location: ExchangeLocation,
    val stateExchangeLockerDeposit: String,
    val stateExchangeLockerRetrieve: String
)

fun ExchangeLockerDto.toExchangeLocker(): ExchangeLocker {
    return ExchangeLocker(
        pinDeposit = pinDeposit,
        pinRetrieve = pinRetrieve,
        lockerDepositId = lockerDepositId,
        lockerRetrieveId = lockerRetrieveId,
        location = location,
        stateExchangeLockerDeposit = stateExchangeLockerDeposit,
        stateExchangeLockerRetrieve = stateExchangeLockerRetrieve
    )
}
