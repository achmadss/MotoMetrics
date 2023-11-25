package com.achmadss.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.achmadss.data.entities.base.Vehicle
import java.time.LocalDateTime

@Entity
data class Motorcycle(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val engine: String,
    val suspension: String,
    val transmission: String,
    override val name: String,
    override val stock: Int,
    override val releaseDate: LocalDateTime,
    override val color: String,
    override val price: Double
) : Vehicle(name, stock, releaseDate, color, price)
