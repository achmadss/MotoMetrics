package com.achmadss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achmadss.data.entities.Transaction
import com.achmadss.data.entities.base.VehicleType

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNewTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<Transaction>

    @androidx.room.Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE vehicleId = :vehicleId AND vehicleType = :vehicleType
    """)
    fun getTransactionsByVehicleIdAndVehicleType(vehicleId: Long, vehicleType: VehicleType): List<Transaction>

}