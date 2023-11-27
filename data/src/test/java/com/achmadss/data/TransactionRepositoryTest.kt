package com.achmadss.data

import androidx.room.Room
import com.achmadss.data.database.LocalDataSource
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.Transaction
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
class TransactionRepositoryTest {

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
    fun `(1) create new successful transaction`() {
        val newCar = db.vehicleDao().getAllCars().first()
        val newMotorcycle = db.vehicleDao().getAllMotorcycles().first()
        testTransaction(newCar.id, VehicleType.CAR)
        testTransaction(newMotorcycle.id, VehicleType.MOTORCYCLE)
    }

    @Test
    fun `(2) reduce vehicle stock`() {
        val newCar = db.vehicleDao().getAllCars().first()
        val newMotorcycle = db.vehicleDao().getAllMotorcycles().first()
        val reduceCarStockResult = db.vehicleDao().reduceCarStock(newCar.id)
        assertEquals(1, reduceCarStockResult)
        val reduceMotorcycleStockResult = db.vehicleDao().reduceMotorcycleStock(newMotorcycle.id)
        assertEquals(1, reduceMotorcycleStockResult)
    }

    private fun testTransaction(vehicleId: Long, vehicleType: VehicleType) {
        db.runInTransaction {
            when (vehicleType) {
                VehicleType.CAR -> db.vehicleDao().reduceCarStock(vehicleId)
                VehicleType.MOTORCYCLE -> db.vehicleDao().reduceMotorcycleStock(vehicleId)
            }
            val transactionResult = db.transactionDao().createNewTransaction(
                Transaction(vehicleId = vehicleId, vehicleType = vehicleType)
            )
            assertTrue(transactionResult != -1L)
        }
    }
}
