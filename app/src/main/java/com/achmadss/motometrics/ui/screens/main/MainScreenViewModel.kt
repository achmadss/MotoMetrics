package com.achmadss.motometrics.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.DataState
import com.achmadss.data.entities.base.VehicleInfo
import com.achmadss.data.repositories.VehicleRepository
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

    private var originalVehicleInfos = listOf<VehicleInfo>()

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
                    originalVehicleInfos = result.data
                    it.copy(loading = false, vehicles = originalVehicleInfos)
                }
            }
        }
    }

    fun changeTab(tabType: TabType) {
        _currentTab.update { tabType }
    }

}
