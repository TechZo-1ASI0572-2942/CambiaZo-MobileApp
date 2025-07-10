package com.techzo.cambiazo.data.repository

import com.techzo.cambiazo.common.Resource
import com.techzo.cambiazo.data.remote.exchangelocker.ExchangeLockerService
import com.techzo.cambiazo.data.remote.exchangelocker.toExchangeLocker
import com.techzo.cambiazo.domain.ExchangeLocker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeLockerRepository(private val exchangeLockerService: ExchangeLockerService) {

    suspend fun getExchangeLockerByExchangeIdAndUserId(
        exchangeId: Int,
        userId: Int
    ): Resource<ExchangeLocker> = withContext(Dispatchers.IO) {
        try {
            val response = exchangeLockerService.getExchangeLockerByExchangeIdAndUserId(exchangeId, userId)
            if (response.isSuccessful) {
                response.body()?.let { exchangeLockerDto ->
                    return@withContext Resource.Success(data = exchangeLockerDto.toExchangeLocker())
                }
                return@withContext Resource.Error("No se encontró el locker de intercambio")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "Ocurrió un error al obtener el locker")
        }
    }
}