package com.techzo.cambiazo.data.repository

import com.techzo.cambiazo.common.Resource
import com.techzo.cambiazo.data.remote.lockerlocation.LockerLocationService
import com.techzo.cambiazo.data.remote.lockerlocation.toLockerLocation
import com.techzo.cambiazo.domain.LockerLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LockerLocationRepository(private val lockerLocationService: LockerLocationService) {

    suspend fun getLockerLocations(): Resource<List<LockerLocation>> = withContext(Dispatchers.IO) {
        try {
            val response = lockerLocationService.getLockerLocations()
            if (response.isSuccessful) {
                response.body()?.let { lockerLocationsDto ->
                    val lockerLocations = lockerLocationsDto.map { it.toLockerLocation() }
                    return@withContext Resource.Success(data = lockerLocations)
                }
                return@withContext Resource.Error("No se encontraron ubicaciones de lockers")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "Ocurrió un error")
        }
    }

    suspend fun getLockerLocationById(id: Int): Resource<LockerLocation> = withContext(Dispatchers.IO) {
        try {
            val response = lockerLocationService.getLockerLocationById(id)
            if (response.isSuccessful) {
                response.body()?.let { lockerLocationDto ->
                    return@withContext Resource.Success(data = lockerLocationDto.toLockerLocation())
                }
                return@withContext Resource.Error("Ubicación no encontrada")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "Ocurrió un error")
        }
    }
}