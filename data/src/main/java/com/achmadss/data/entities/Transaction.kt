package com.achmadss.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.achmadss.data.entities.base.VehicleType
import java.time.LocalDateTime

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val vehicleId: Long,
    val vehicleType: VehicleType,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
