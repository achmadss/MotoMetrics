package com.achmadss.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.CarWithTransactions
import com.achmadss.data.entities.MotorcycleWithTransactions

@Dao
interface VehicleDao {

    // car

    @Upsert
    fun upsertCar(car: Car): Long

    @Query("SELECT * FROM Car")
    fun getAllCars(): List<Car>

    @Query("SELECT * FROM Car WHERE id = :id")
    fun getCarById(id: Long): Car?

    @Transaction
    @Query("SELECT * FROM Car WHERE id = :id")
    fun getCarWithTransactions(id: Long): CarWithTransactions

    @Query("UPDATE Car SET stock = stock - 1 WHERE id = :id AND stock > 0")
    fun reduceCarStock(id: Long)

    // motorcycle

    @Upsert
    fun upsertMotorcycle(motorcycle: Motorcycle): Long

    @Query("SELECT * FROM Motorcycle")
    fun getAllMotorcycles(): List<Motorcycle>

    @Query("SELECT * FROM Motorcycle WHERE id = :id")
    fun getMotorcycleById(id: Long): Motorcycle?

    @Transaction
    @Query("SELECT * FROM Motorcycle WHERE id = :id")
    fun getMotorcycleWithTransactions(id: Long): MotorcycleWithTransactions

    @Query("UPDATE Motorcycle SET stock = stock - 1 WHERE id = :id AND stock > 0")
    fun reduceMotorcycleStock(id: Long)

}