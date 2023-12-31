package com.achmadss.data.repositories

import com.achmadss.data.DataState
import com.achmadss.data.database.LocalDataSourceProvider
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
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
        emit(DataState.Success((cars + motorcycles).sortedByDescending { it.createdAt }))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

    fun getVehicleByIdAndType(
        id: Long,
        vehicleType: VehicleType,
    ) = flow {
        emit(DataState.Loading)
        val vehicle = checkNotNull(
            when(vehicleType) {
                VehicleType.CAR -> LocalDataSourceProvider.vehicleDao().getCarById(id)
                VehicleType.MOTORCYCLE -> LocalDataSourceProvider.vehicleDao().getMotorcycleById(id)
            }
        )
        emit(DataState.Success(vehicle))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

}