package com.achmadss.data.entities

import com.achmadss.data.entities.base.Vehicle

data class TransactionWithVehicle(
    val transaction: Transaction,
    val vehicle: Vehicle?,
)
