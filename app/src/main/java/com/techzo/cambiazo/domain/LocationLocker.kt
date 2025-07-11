package com.techzo.cambiazo.domain

data class LocationLocker(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val address: String,
    val districtId: Int
)