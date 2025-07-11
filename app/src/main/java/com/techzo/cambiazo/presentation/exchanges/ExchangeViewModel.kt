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
import com.techzo.cambiazo.data.repository.ExchangeLockerRepository
import com.techzo.cambiazo.data.repository.ExchangeRepository
import com.techzo.cambiazo.data.repository.LocationRepository
import com.techzo.cambiazo.data.repository.ReviewRepository
import com.techzo.cambiazo.domain.Department
import com.techzo.cambiazo.domain.District
import com.techzo.cambiazo.domain.Exchange
import com.techzo.cambiazo.domain.ExchangeLocker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ExchangeViewModel @Inject constructor(private val exchangeRepository: ExchangeRepository,
    private val locationRepository: LocationRepository,
    private val reviewRepository: ReviewRepository,
    private val exchangeLockerRepository: ExchangeLockerRepository,
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


    private val _existReview= mutableStateOf(false)
    val existReview: State<Boolean> get() = _existReview

    private val _exchangeLocker = mutableStateOf(UIState<ExchangeLocker>())
    val exchangeLocker: State<UIState<ExchangeLocker>> get() = _exchangeLocker
    //val exchangeLocker: State<UIState<Exchange>> get() = _exchangeLocker
    //val to open and close the dialog


    init{
        getLocations()
    }

    fun fetchExchanges(page:Int){
        when(page){
            0 -> getAllExchanges()
            2 -> getFinishedExchanges()
        }
    }

    private fun getLocations(){
        viewModelScope.launch {
            _districts.value = locationRepository.getDistricts().data?: emptyList()
            _departments.value = locationRepository.getDepartments().data?: emptyList()
        }
    }

    fun getExchangeLocker(exchangeId: Int) {
        _exchangeLocker.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeLockerRepository.getExchangeLockerByExchangeIdAndUserId(exchangeId, Constants.user!!.id)
            if (result is Resource.Success) {
                _exchangeLocker.value = UIState(data = result.data)
            } else {
                _exchangeLocker.value = UIState(message = result.message ?: "Ocurrió un error")
            }
        }
    }

    fun getAllExchanges() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getAllExchanges()
            if (result is Resource.Success) {
                val filteredData = result.data?.filter { exchange ->
                    exchange.status == "Aceptado" && (exchange.userOwn.id == Constants.user!!.id || exchange.userChange.id == Constants.user!!.id)
                }?.map { exchange ->
                    if (exchange.userChange.id == Constants.user!!.id) {
                        exchange.copy(
                            productOwn = exchange.productChange,
                            productChange = exchange.productOwn,
                            userOwn = exchange.userChange,
                            userChange = exchange.userOwn
                        )
                    } else {
                        exchange
                    }
                }

                //print
                Log.d("ExchangeViewModel", "getAllExchanges: ${filteredData} exchanges found")
                _state.value = UIState(data = filteredData)
            } else {
                _state.value = UIState(message = result.message ?: "Ocurrió un error")
            }
        }
    }

    fun getExchangesByUserOwnId() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = exchangeRepository.getExchangesByUserOwnId(Constants.user!!.id)
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

                val filteredData = result.data?.map { exchange ->
                    if (exchange.userChange.id == Constants.user!!.id) {
                        exchange.copy(
                            productOwn = exchange.productChange,
                            productChange = exchange.productOwn,
                            userOwn = exchange.userChange,
                            userChange = exchange.userOwn
                        )
                    } else {
                        exchange
                    }
                }
                _finishedExchanges.value = UIState(data = filteredData)

                _state.value = UIState(data = filteredData)
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
                _existReview.value= reviewRepository.getReviewsByUserAuthorIdAndExchangeId(Constants.user!!.id, exchangeId).data!!
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