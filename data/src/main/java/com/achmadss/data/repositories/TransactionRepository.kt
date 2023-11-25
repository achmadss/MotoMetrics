package com.achmadss.data.repositories

import com.achmadss.data.DataState
import com.achmadss.data.database.LocalDataSourceProvider
import com.achmadss.data.entities.Car
import com.achmadss.data.entities.Motorcycle
import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.TransactionWithVehicle
import com.achmadss.data.entities.base.VehicleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object TransactionRepository {

    fun upsertTransaction(transaction: Transaction) = flow {
        emit(DataState.Loading)
        val result = LocalDataSourceProvider.transactionDao().upsertTransaction(transaction)
        if (result == -1L) throw Error("Upsert failed")
        emit(DataState.Success(result))
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

    fun getAllTransactionsWithVehicles() = flow {
        val allTransaction = LocalDataSourceProvider.transactionDao().getAllTransactions()
        val allCars = LocalDataSourceProvider.vehicleDao().getAllCars().associateBy { it.id }
        val allMotorcycles = LocalDataSourceProvider.vehicleDao().getAllMotorcycles().associateBy { it.id }
        val transactionsWithVehicles = allTransaction.map {
            val vehicle = when(it.vehicleType) {
                VehicleType.Car -> allCars[it.vehicleId]
                VehicleType.Motorcycle -> allMotorcycles[it.vehicleId]
            }
            TransactionWithVehicle(it, vehicle)
        }
        emit(DataState.Success(transactionsWithVehicles))
    }.catch { DataState.Error(it) }.flowOn(Dispatchers.IO)

}