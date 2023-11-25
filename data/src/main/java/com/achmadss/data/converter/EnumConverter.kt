package com.achmadss.data.converter

import androidx.room.TypeConverter
import com.achmadss.data.entities.base.VehicleType

class EnumConverter {

    @TypeConverter
    fun fromVehicleType(value: VehicleType): String {
        return value.name
    }

    @TypeConverter
    fun toVehicleType(value: String): VehicleType {
        return VehicleType.valueOf(value)
    }

}