package com.achmadss.data

import androidx.room.Room
import com.achmadss.data.database.LocalDataSource
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VehicleRepositoryTest {

    private lateinit var db: LocalDataSource

    private val car = Car(
        engine = "test engine",
        capacity = 3,
        type = "test type",
        color = "red",
        name = "car 1",
        price = 100000,
        stock = 10,
        releaseDate = LocalDateTime.now(),
        vehicleType = VehicleType.CAR
    )

    private val motorcycle = Motorcycle(
        engine = "test engine",
        suspension = "manual",
        transmission = "test transmission",
        color = "red",
        name = "motor 1",
        price = 50000,
        stock = 10,
        releaseDate = LocalDateTime.now(),
        vehicleType = VehicleType.MOTORCYCLE
    )

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            LocalDataSource::class.java
        ).allowMainThreadQueries().build()

        db.vehicleDao().upsertCar(car)
        db.vehicleDao().upsertMotorcycle(motorcycle)
    }

    @After
    fun cleanup() {
        db.close()
    }
    @Test
    fun `(1) upsert a car`() {
        val result = db.vehicleDao().upsertCar(car)
        assertTrue(result != -1L)
    }

    @Test
    fun `(2) upsert a motorcycle`() {
        val result = db.vehicleDao().upsertMotorcycle(motorcycle)
        assertTrue(result != -1L)
    }

    @Test
    fun `(3) get all vehicles`() {
        val expectedVehicles = listOf(car.copy(id = 1L), motorcycle.copy(id = 1L))
        val cars = db.vehicleDao().getAllCars()
        val motorcycles = db.vehicleDao().getAllMotorcycles()
        assertEquals(expectedVehicles, cars + motorcycles)
    }

    @Test
    fun `(4) get vehicle by id and type`() {

        fun getVehicleByIdAndType(id: Long, vehicleType: VehicleType): Vehicle {
            return checkNotNull(
                when(vehicleType) {
                    VehicleType.CAR -> db.vehicleDao().getCarById(id)
                    VehicleType.MOTORCYCLE -> db.vehicleDao().getMotorcycleById(id)
                }
            )
        }

        assertTrue(getVehicleByIdAndType(1L, VehicleType.CAR) is Car)
        assertTrue(getVehicleByIdAndType(1L, VehicleType.MOTORCYCLE) is Motorcycle)
    }

}