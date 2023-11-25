package com.achmadss.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MotorcycleWithTransactions(
    @Embedded val motorcycle: Motorcycle,
    @Relation(
        parentColumn = "id",
        entityColumn = "vehicleId",
        entity = Transaction::class
    )
    val transactions: List<Transaction>
)