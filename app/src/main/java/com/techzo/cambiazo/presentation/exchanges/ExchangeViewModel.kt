package com.techzo.cambiazo.presentation.exchanges

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.techzo.cambiazo.common.Constants
import com.techzo.cambiazo.common.Resource
import com.techzo.cambiazo.common.UIState
import com.techzo.cambiazo.data.repository.ExchangeRepository
import com.techzo.cambiazo.data.repository.LocationRepository
import com.techzo.cambiazo.domain.Department
import com.techzo.cambiazo.domain.District
import com.techzo.cambiazo.domain.Exchange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ExchangeViewModel @Inject constructor(private val exchangeRepository: ExchangeRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _exchangesSend = mutableStateOf(UIState<List<Exchange>>())
    //val exchangesSend: State<UIState<List<Exchange>>> get() = _exchangesSend

    private val _exchangesReceived = mutableStateOf(UIState<List<Exchange>>())
    //val exchangesReceived: State<UIState<List<Exchange>>> get() = _exchangesReceived

    private val _finishedExchanges = mutableStateOf(UIState<List<Exchange>>())
    //val finishedExchanges: State<UIState<List<Exchange>>> get() = _finishedExchanges

    private val _state = mutableStateOf(UIState<List<Exchange>>())
    val state: State<UIState<List<Exchange>>> get() = _state

    private val _exchange = mutableStateOf(UIState<Exchange>())
    val exchange: State<UIState<Exchange>> get() = _exchange

    private val _districts = mutableStateOf<List<District>>(emptyList())
    //val districts: State<List<District>> get() = _districts

    private val _departments = mutableStateOf<List<Department>>(emptyList())
    //val departments: State<List<Department>> get() = _departments

    init{

        getLocations()
    }

    fun fetchExchanges(page:Int){
        when(page){
            0 -> getExchangesByUserOwnId()
            1 -> getExchangesByUserChangeId()
            2 -> getFinishedExchanges()
        }
    }

    private fun getLocations(){
        viewModelScope.launch {
            _districts.value = locationRepository.getDistricts().data?: emptyList()
            _departments.value = locationRepository.getDepartments().data?: emptyList()
        }
    }

    fun getExchangesByUserOwnId() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getExchangesByUserOwnId(Constants.user!!.id)
            Log.d("id", Constants.user?.id.toString())
            if (result is Resource.Success) {
                val filteredData = result.data?.filter { exchange ->
                    exchange.productOwn.available && exchange.productChange.available &&
                            exchange.userOwn.name != "Usuario de cambio" && exchange.userChange.name != "Usuario de cambio"
                }
                _exchangesSend.value = UIState(data = filteredData)
                _state.value = UIState(data = filteredData)
            } else {
                _state.value = UIState(message = result.message ?: "Ocurrió un error")
            }
            Log.d("ExchangeViewModel", "getExchangesByUserOwnId: ${result.data}")
        }
    }

    fun getExchangesByUserChangeId() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getExchangesByUserChangeId(Constants.user!!.id)
            if (result is Resource.Success) {
                val filteredData = result.data?.filter { exchange ->
                    exchange.productOwn.available && exchange.productChange.available &&
                            exchange.userOwn.name != "Usuario de cambio" && exchange.userChange.name != "Usuario de cambio"
                }
                _exchangesReceived.value = UIState(data = filteredData)
                _state.value = UIState(data = filteredData)
            } else {
                _state.value = UIState(message = result.message ?: "Ocurrió un error")
            }
        }
    }

    fun getFinishedExchanges() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getFinishedExchanges(Constants.user!!.id)
            if(result is Resource.Success){
                _finishedExchanges.value = UIState(data = result.data)
                _state.value = UIState(data = result.data)
            }else{
                _state.value = UIState(message = result.message?:"Ocurrió un error")
            }
            Log.d("ExchangeViewModel", "getFinishedExchanges: ${result.data}")
        }
    }

    fun getExchangeById(exchangeId: Int) {
        _exchange.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getExchangeById(exchangeId)
            if(result is Resource.Success){
                _exchange.value = UIState(data = result.data)
            }else{
                _exchange.value = UIState(message = result.message?:"Ocurrió un error")
            }
        }
    }

    fun getLocationString(districtId: Int): String {
        val district = _districts.value.find { it.id == districtId }
        val department = _departments.value.find { it.id == district?.departmentId }
        return "${district?.name}, ${department?.name}"
    }

    fun updateExchangeStatus(exchangeId: Int, status: String){
        viewModelScope.launch {
            val result = exchangeRepository.updateExchangeStatus(exchangeId, status)
            if(result is Resource.Success){
                Log.d("ExchangeViewModel", "updateExchangeStatus: ${result.data}")
            }else{
                Log.d("ExchangeViewModel", "updateExchangeStatus: ${result.message}")
            }
        }
    }

    fun deleteExchange(exchangeId: Int){
        viewModelScope.launch {
            val result = exchangeRepository.deleteExchange(exchangeId)
            if(result is Resource.Success){
                Log.d("ExchangeViewModel", "deleteExchange: ${result.data}")
            }else{
                Log.d("ExchangeViewModel", "deleteExchange: ${result.message}")
            }
        }
    }

}