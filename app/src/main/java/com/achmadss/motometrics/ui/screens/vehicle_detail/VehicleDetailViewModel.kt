package com.achmadss.motometrics.ui.screens.vehicle_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.DataState
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.data.repositories.TransactionRepository
import com.achmadss.data.repositories.VehicleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleDetailUIState())
    val uiState: StateFlow<VehicleDetailUIState> = _uiState.asStateFlow()

    fun getVehicleByIdAndType(
        id: Long?,
        vehicleType: VehicleType
    ) = viewModelScope.launch {
        if (id == null) {
            _uiState.update { it.copy(errorMessage = "Failed to fetch Vehicle: ID cannot be null") }
            return@launch
        }
        VehicleRepository.getVehicleByIdAndType(id, vehicleType).collect { result ->
            when(result) {
                is DataState.Error -> _uiState.update {
                    it.copy(loading = false, errorMessage = result.error.message ?: it.errorMessage)
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(loading = true) }
                    delay(1000)
                }
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            vehicle = result.data,
                        )
                    }
                }
            }
        }
    }

    fun getTransactionsByVehicleIdAndVehicleType(
        id: Long?,
        vehicleType: VehicleType
    ) = viewModelScope.launch {
        if (id == null) {
            _uiState.update { it.copy(errorMessage = "Failed to fetch Vehicle: ID cannot be null") }
            return@launch
        }
        TransactionRepository.getTransactionsByVehicleIdAndVehicleType(id, vehicleType).collect { result ->
            when(result) {
                is DataState.Error -> _uiState.update {
                    it.copy(loading = false, errorMessage = result.error.message ?: it.errorMessage)
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(loading = true) }
                    delay(1000)
                }
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            transactions = result.data
                        )
                    }
                }
            }
        }
    }

}
