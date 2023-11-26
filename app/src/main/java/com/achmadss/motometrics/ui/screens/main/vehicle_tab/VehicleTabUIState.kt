package com.achmadss.motometrics.ui.screens.main.vehicle_tab

import com.achmadss.data.entities.base.VehicleInfo
import com.achmadss.motometrics.base.BaseUIState

data class VehicleTabUIState(
    val vehicleInfos: List<VehicleInfo> = listOf(),
    override val loading: Boolean = false,
    override val errorMessage: String = "Unknown error"
): BaseUIState