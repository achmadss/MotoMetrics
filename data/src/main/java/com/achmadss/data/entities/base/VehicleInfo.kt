package com.achmadss.data.entities.base

import java.time.LocalDateTime

data class VehicleInfo(
    val id: Long,
    val name: String,
    val stock: Int,
    val createdAt: LocalDateTime,
)
