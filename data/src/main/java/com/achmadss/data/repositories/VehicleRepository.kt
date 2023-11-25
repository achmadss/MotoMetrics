package com.achmadss.data.repositories

import com.achmadss.data.DataState
import com.achmadss.data.database.LocalDataSourceProvider
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object VehicleRepository {

    fun upsertVehicle(vehicle: Vehicle) = flow {
        emit(DataState.Loading)
        val result = when(vehicle) {
            is Car -> LocalDataSourceProvider.vehicleDao().upsertCar(vehicle)
            is Motorcycle -> LocalDataSourceProvider.vehicleDao().upsertMotorcycle(vehicle)
            else -> -1L
        }
        if (result == -1L) throw Error("Upsert failed")
        emit(DataState.Success(result))
    }.catch { DataState.Error(it) }.flowOn(Dispatchers.IO)

    fun getAllVehicles() = flow {
        emit(DataState.Loading)
        val cars = LocalDataSourceProvider.vehicleDao().getAllCars()
        val motorcycles = LocalDataSourceProvider.vehicleDao().getAllMotorcycles()
        emit(DataState.Success(Pair(cars, motorcycles)))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

    fun getCarWithTransactions(
        id: Long,
    ) = flow {
        emit(DataState.Loading)
        emit(DataState.Success(LocalDataSourceProvider.vehicleDao().getCarWithTransactions(id)))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

    fun getMotorcycleWithTransactions(
        id: Long,
    ) = flow {
        emit(DataState.Loading)
        emit(DataState.Success(LocalDataSourceProvider.vehicleDao().getMotorcycleWithTransactions(id)))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

}