package com.achmadss.data.entities.base

import java.time.LocalDateTime

open class Vehicle(
    open val name: String,
    open val stock: Int,
    open val releaseDate: LocalDateTime,
    open val color: String,
    open val price: Double,
)
