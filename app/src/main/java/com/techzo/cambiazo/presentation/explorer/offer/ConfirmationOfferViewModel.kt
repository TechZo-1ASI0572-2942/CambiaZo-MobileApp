package com.techzo.cambiazo.presentation.explorer.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.techzo.cambiazo.domain.Product
import com.techzo.cambiazo.data.remote.exchanges.ExchangeRequestDto
import com.techzo.cambiazo.data.repository.ExchangeRepository
import com.techzo.cambiazo.common.Resource
import com.techzo.cambiazo.data.repository.LockerLocationRepository
import com.techzo.cambiazo.data.repository.ProductRepository
import com.techzo.cambiazo.domain.LocationLocker
import com.techzo.cambiazo.domain.LockerLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationOfferViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val exchangeRepository: ExchangeRepository,
    private val lockerLocationRepository: LockerLocationRepository
) : ViewModel() {

    private val _desiredProduct = MutableStateFlow<Product?>(null)
    val desiredProduct: StateFlow<Product?> get() = _desiredProduct

    private val _offeredProduct = MutableStateFlow<Product?>(null)
    val offeredProduct: StateFlow<Product?> get() = _offeredProduct

    private val _offerSuccess = MutableStateFlow(false)
    val offerSuccess: StateFlow<Boolean> get() = _offerSuccess

    private val _offerFailure = MutableStateFlow(false)
    val offerFailure : StateFlow<Boolean> get() = _offerFailure

    private val _locationLocker = MutableStateFlow<LockerLocation?>(null)
    val locationLocker: StateFlow<LockerLocation?> get() = _locationLocker

    init {
        viewModelScope.launch {
            val desiredProductId: Int? = savedStateHandle.get<String>("desiredProductId")?.toIntOrNull()
            val offeredProductId: Int? = savedStateHandle.get<String>("offeredProductId")?.toIntOrNull()
            val locationLockerId: Int? = savedStateHandle.get<String>("locationId")?.toIntOrNull()

            if (desiredProductId != null && offeredProductId != null &&  locationLockerId != null) {
                val desiredProdResource = productRepository.getProductById(desiredProductId)
                val offeredProdResource = productRepository.getProductById(offeredProductId)
                val locationLockerResource = lockerLocationRepository.getLockerLocationById(locationLockerId)

                _desiredProduct.value = (desiredProdResource as? Resource.Success)?.data
                _offeredProduct.value = (offeredProdResource as? Resource.Success)?.data
                _locationLocker.value = (locationLockerResource as? Resource.Success)?.data
            }
        }
    }

    fun makeOffer() {
        val desiredProduct = _desiredProduct.value
        val offeredProduct = _offeredProduct.value
        val locationLocker = _locationLocker.value

        viewModelScope.launch {
            val newExchangeRequest = ExchangeRequestDto(
                productOwnId = offeredProduct!!.id,
                productChangeId = desiredProduct!!.id,
                status = "Pendiente",
                locationId = locationLocker!!.id
            )

            val result = exchangeRepository.createExchange(newExchangeRequest)
            if (result is Resource.Success) _offerSuccess.value = true else _offerFailure.value = true
        }
    }
}