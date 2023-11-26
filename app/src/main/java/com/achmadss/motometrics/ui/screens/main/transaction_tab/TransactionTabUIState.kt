package com.achmadss.motometrics.ui.screens.main.transaction_tab

import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.TransactionWithVehicle
import com.achmadss.motometrics.base.BaseUIState

data class TransactionTabUIState(
    val transactionsWithVehicles: List<TransactionWithVehicle> = listOf(),
    val showAddTransactionDialog: Boolean = false,
    val cars: List<Car> = listOf(),
    val motorcycles: List<Motorcycle> = listOf(),
    override val loading: Boolean = false,
    override val errorMessage: String = "Unknown error"
): BaseUIState
