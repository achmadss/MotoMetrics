package com.achmadss.motometrics.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.DataState
import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.data.repositories.TransactionRepository
import com.achmadss.data.repositories.VehicleRepository
import com.achmadss.motometrics.ui.screens.main.transaction_tab.TransactionTabUIState
import com.achmadss.motometrics.ui.screens.main.vehicle_tab.VehicleTabUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    private val _currentTab = MutableStateFlow(TabType.Vehicle)
    val currentTab: StateFlow<TabType> = _currentTab.asStateFlow()

    private val _vehicleTabUIState = MutableStateFlow(VehicleTabUIState())
    val vehicleTabUIState: StateFlow<VehicleTabUIState> = _vehicleTabUIState.asStateFlow()

    private val _transactionTabUIState = MutableStateFlow(TransactionTabUIState())
    val transactionTabUIState: StateFlow<TransactionTabUIState> = _transactionTabUIState.asStateFlow()

    private var originalVehicles = listOf<Vehicle>()

    fun changeTab(tabType: TabType) {
        _currentTab.update { tabType }
    }

    fun changeAddTransactionDialogVisibility(visible: Boolean) {
        _transactionTabUIState.update {
            it.copy(showAddTransactionDialog = visible)
        }
    }

    fun getAllVehicles() = viewModelScope.launch {
        VehicleRepository.getAllVehicles().collect { result ->
            when(result) {
                is DataState.Error -> _vehicleTabUIState.update {
                    it.copy(loading = false, errorMessage = result.error.message ?: it.errorMessage)
                }
                is DataState.Loading -> {
                    _vehicleTabUIState.update { it.copy(loading = true) }
                    delay(1000)
                }
                is DataState.Success -> _vehicleTabUIState.update {
                    originalVehicles = result.data
                    it.copy(loading = false, vehicles = originalVehicles)
                }
            }
        }
    }

    fun getAllTransactionsWithVehicles() = viewModelScope.launch {
        TransactionRepository.getAllTransactionsWithVehicles().collect { result ->
            when(result) {
                is DataState.Error -> _transactionTabUIState.update {
                    it.copy(loading = false, errorMessage = result.error.message ?: it.errorMessage)
                }
                is DataState.Loading -> {
                    _transactionTabUIState.update { it.copy(loading = true) }
                    delay(1000)
                }
                is DataState.Success -> _transactionTabUIState.update {
                    it.copy(loading = false, transactionsWithVehicles = result.data)
                }
            }
        }
    }

    fun createNewTransaction(
        vehicleId: Long,
        vehicleType: VehicleType,
        callback: (Boolean, String) -> Unit,
    ) = viewModelScope.launch {
        TransactionRepository.createNewTransaction(
            Transaction(vehicleId = vehicleId, vehicleType = vehicleType)
        ).collect { result ->
            when(result) {
                is DataState.Error -> callback(false, result.error.message ?: "Unknown error")
                is DataState.Loading -> Unit
                is DataState.Success -> {
                    callback(true, "success")
                    getAllVehicles()
                    getAllTransactionsWithVehicles()
                }
            }
        }
    }

    fun searchCarByName(query: String) {
        if (query.isBlank()) {
            _vehicleTabUIState.update {
                it.copy(vehicles = originalVehicles)
            }
            return
        }
        val filteredData = originalVehicles.filter { it.name.contains(query) }
        _vehicleTabUIState.update { it.copy(vehicles = filteredData) }
    }

}
