package com.achmadss.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle

@Dao
interface VehicleDao {

    // car
    @Upsert
    fun upsertCar(car: Car): Long

    @Query("SELECT * FROM Car")
    fun getAllCars(): List<Car>

    @Query("SELECT * FROM Car WHERE id = :id")
    fun getCarById(id: Long): Car?

    @Query("UPDATE Car SET stock = stock - 1 WHERE id = :id AND stock > 0")
    fun reduceCarStock(id: Long): Int

    // motorcycle
    @Upsert
    fun upsertMotorcycle(motorcycle: Motorcycle): Long

    @Query("SELECT * FROM Motorcycle")
    fun getAllMotorcycles(): List<Motorcycle>

    @Query("SELECT * FROM Motorcycle WHERE id = :id")
    fun getMotorcycleById(id: Long): Motorcycle?

    @Query("UPDATE Motorcycle SET stock = stock - 1 WHERE id = :id AND stock > 0")
    fun reduceMotorcycleStock(id: Long): Int

}