package com.achmadss.motometrics.ui.screens.main.vehicle_tab

import com.achmadss.data.entities.base.Vehicle
import com.achmadss.motometrics.base.BaseUIState

data class VehicleTabUIState(
    val vehicles: List<Vehicle> = listOf(),
    override val loading: Boolean = false,
    override val errorMessage: String = ""
): BaseUIState