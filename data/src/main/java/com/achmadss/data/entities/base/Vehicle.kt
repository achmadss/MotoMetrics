package com.achmadss.data.entities.base

import java.time.LocalDateTime

open class Vehicle(
    open val releaseDate: LocalDateTime,
    open val color: String,
    open val price: Double,
)
