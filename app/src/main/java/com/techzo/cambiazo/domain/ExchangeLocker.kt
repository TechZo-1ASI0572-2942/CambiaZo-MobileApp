package com.techzo.cambiazo.domain

data class ExchangeLocker(
    val pinDeposit: String,
    val pinRetrieve: String,
    val lockerDepositId: String,
    val lockerRetrieveId: String,
    val location: ExchangeLocation,
    val stateExchangeLockerDeposit: String,
    val stateExchangeLockerRetrieve: String
)

data class ExchangeLocation(
    val districtId: Int,
    val districtName: String,
    val departmentId: Int,
    val departmentName: String,
    val countryId: Int,
    val countryName: String
)