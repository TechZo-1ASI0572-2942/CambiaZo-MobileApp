package com.techzo.cambiazo.data.remote.lockerlocation

import com.techzo.cambiazo.domain.LockerLocation

data class LockerLocationDto(
    val id: Int,
    val name: String,
    val address: String,
    val districtId: Int
)


fun LockerLocationDto.toLockerLocation(): LockerLocation {
    return LockerLocation(
        id = id,
        name = name,
        address = address,
        districtId = districtId
    )
}