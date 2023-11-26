package com.achmadss.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.achmadss.data.entities.base.Vehicle
import java.time.LocalDateTime

@Entity
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val engine: String,
    val capacity: Int,
    val type: String,
    override val name: String,
    override val stock: Int,
    override val releaseDate: LocalDateTime,
    override val color: String,
    override val price: Int
) : Vehicle(name, stock, releaseDate, color, price)
