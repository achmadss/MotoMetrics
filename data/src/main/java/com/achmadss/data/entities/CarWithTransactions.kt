package com.achmadss.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CarWithTransactions(
    @Embedded val car: Car,
    @Relation(
        parentColumn = "id",
        entityColumn = "vehicleId",
        entity = Transaction::class
    )
    val transactions: List<Transaction>
)
