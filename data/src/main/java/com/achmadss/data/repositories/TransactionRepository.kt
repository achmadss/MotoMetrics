package com.achmadss.data.repositories

import com.achmadss.data.DataState
import com.achmadss.data.database.LocalDataSourceProvider
import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.TransactionWithVehicle
import com.achmadss.data.entities.base.Vehicle
import com.achmadss.data.entities.base.VehicleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object TransactionRepository {

    fun createNewTransaction(transaction: Transaction) = flow {
        emit(DataState.Loading)
        handleVehicleTransaction(transaction)
        val result = LocalDataSourceProvider.transactionDao().createNewTransaction(transaction)
        if (result == -1L) throw Error("Create new transaction failed")
        emit(DataState.Success(result))
    }.catch {
        emit(DataState.Error(it))
    }.flowOn(Dispatchers.IO)

    private fun handleVehicleTransaction(transaction: Transaction) {
        when (transaction.vehicleType) {
            VehicleType.CAR -> reduceStockIfAvailable(
                LocalDataSourceProvider.vehicleDao()::getCarById,
                LocalDataSourceProvider.vehicleDao()::reduceCarStock,
                transaction.vehicleId
            )
            VehicleType.MOTORCYCLE -> reduceStockIfAvailable(
                LocalDataSourceProvider.vehicleDao()::getMotorcycleById,
                LocalDataSourceProvider.vehicleDao()::reduceMotorcycleStock,
                transaction.vehicleId
            )
        }
    }

    private inline fun <reified T : Vehicle> reduceStockIfAvailable(
        getVehicle: (Long) -> T?,
        reduceStock: (Long) -> Unit,
        vehicleId: Long
    ) {
        val vehicle = getVehicle(vehicleId)
        vehicle?.let {
            if (it.stock <= 0) throw Error("${T::class.simpleName} stock is empty")
            reduceStock(vehicleId)
        } ?: throw Error("${T::class.simpleName} not found")
    }


    fun getAllTransactionsWithVehicles() = flow {
        emit(DataState.Loading)
        val allTransaction = LocalDataSourceProvider.transactionDao().getAllTransactions()
        val allCars = LocalDataSourceProvider.vehicleDao().getAllCars().associateBy { it.id }
        val allMotorcycles = LocalDataSourceProvider.vehicleDao().getAllMotorcycles().associateBy { it.id }
        val transactionsWithVehicles = allTransaction.map {
            val vehicle = when(it.vehicleType) {
                VehicleType.CAR -> allCars[it.vehicleId]
                VehicleType.MOTORCYCLE -> allMotorcycles[it.vehicleId]
            }
            TransactionWithVehicle(it, vehicle)
        }
        emit(DataState.Success(transactionsWithVehicles.sortedByDescending { it.transaction.createdAt }))
    }.catch { DataState.Error(it) }.flowOn(Dispatchers.IO)

    fun getTransactionsByVehicleIdAndVehicleType(
        id: Long,
        vehicleType: VehicleType
    ) = flow {
        emit(DataState.Loading)
        emit(DataState.Success(
            LocalDataSourceProvider.transactionDao()
                .getTransactionsByVehicleIdAndVehicleType(id, vehicleType))
        )
    }.catch { emit(DataState.Error(it)) }.flowOn(Dispatchers.IO)

}