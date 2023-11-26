package com.achmadss.motometrics.ui.screens.add_vehicle

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.DataState
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import com.achmadss.data.repositories.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddVehicleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddVehicleUIState())
    val uiState: StateFlow<AddVehicleUIState> = _uiState.asStateFlow()

    fun addNewVehicle(
        vehicle: Vehicle,
        context: Context,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        VehicleRepository.upsertVehicle(vehicle).collect { result ->
            when(result) {
                is DataState.Error -> {
                    Toast.makeText(context, result.error.message, Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> Unit
                is DataState.Success -> onSuccess()
            }
        }
    }

}
