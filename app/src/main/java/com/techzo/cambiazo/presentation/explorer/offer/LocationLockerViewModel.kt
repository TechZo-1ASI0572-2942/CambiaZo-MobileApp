package com.techzo.cambiazo.presentation.explorer.offer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techzo.cambiazo.common.Constants
import com.techzo.cambiazo.common.Resource
import com.techzo.cambiazo.common.UIState
import com.techzo.cambiazo.data.remote.exchanges.ExchangeRequestDto
import com.techzo.cambiazo.data.repository.ExchangeRepository
import com.techzo.cambiazo.data.repository.LockerLocationRepository
import com.techzo.cambiazo.data.repository.ProductRepository
import com.techzo.cambiazo.domain.LockerLocation
import com.techzo.cambiazo.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LocationLockerViewModel @Inject constructor(
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

    private val _lockerLocations = MutableStateFlow<List<LockerLocation>>(emptyList())
    val lockerLocations: StateFlow<List<LockerLocation>> get() = _lockerLocations

    private val _state = mutableStateOf(UIState<List<LockerLocation>>())
    val state: State<UIState<List<LockerLocation>>> = _state


    init {
        viewModelScope.launch {
            getLockerLocations()
            val desiredProductId: Int? = savedStateHandle.get<String>("desiredProductId")?.toIntOrNull()
            val offeredProductId: Int? = savedStateHandle.get<String>("offeredProductId")?.toIntOrNull()

            if (desiredProductId != null && offeredProductId != null) {
                val desiredProdResource = productRepository.getProductById(desiredProductId)
                val offeredProdResource = productRepository.getProductById(offeredProductId)

                _desiredProduct.value = (desiredProdResource as? Resource.Success)?.data
                _offeredProduct.value = (offeredProdResource as? Resource.Success)?.data
            }
        }
    }

    private fun getLockerLocations() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = lockerLocationRepository.getLockerLocations()
            if (result is Resource.Success) {
                _lockerLocations.value = result.data ?: emptyList()
                _state.value = UIState()
            } else {
                _state.value = UIState(message = result.message ?: "Ocurrió un error al cargar lockers")
            }
        }
    }


}