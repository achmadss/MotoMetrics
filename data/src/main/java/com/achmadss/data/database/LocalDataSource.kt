package com.achmadss.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.achmadss.data.converter.EnumConverter
import com.achmadss.data.converter.LocalDateTimeConverter
import com.achmadss.data.dao.TransactionDao
import com.achmadss.data.dao.VehicleDao
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.Transaction

@Database(
    entities = [
        Car::class,
        Motorcycle::class,
        Transaction::class
    ],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class, EnumConverter::class)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val name = "db"
    }

}