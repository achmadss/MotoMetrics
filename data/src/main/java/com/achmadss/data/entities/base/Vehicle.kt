package com.achmadss.data.entities.base

import java.time.LocalDateTime

open class Vehicle(
    open val name: String,
    open val vehicleType: VehicleType,
    open val stock: Int,
    open val releaseDate: LocalDateTime,
    open val color: String,
    open val price: Int,
    open var createdAt: LocalDateTime = LocalDateTime.now(),
)
