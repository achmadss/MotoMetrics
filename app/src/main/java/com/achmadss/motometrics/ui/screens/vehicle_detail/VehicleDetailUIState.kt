package com.achmadss.motometrics.ui.screens.vehicle_detail

import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.motometrics.base.BaseUIState

data class VehicleDetailUIState(
    val vehicle: Vehicle? = null,
    val transactions: List<Transaction> = listOf(),
    override val loading: Boolean = false,
    override val errorMessage: String = "",
): BaseUIState
