package com.achmadss.motometrics.ui.screens.main.transaction_tab

import com.achmadss.data.entities.TransactionWithVehicle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.motometrics.base.BaseUIState

data class TransactionTabUIState(
    val transactionsWithVehicles: List<TransactionWithVehicle> = listOf(),
    val showAddTransactionDialog: Boolean = false,
    val vehicles: List<Vehicle> = listOf(),
    override val loading: Boolean = false,
    override val errorMessage: String = ""
): BaseUIState
