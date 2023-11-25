package com.achmadss.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
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
abstract class LocalDataSource : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val name = "db"
    }

}