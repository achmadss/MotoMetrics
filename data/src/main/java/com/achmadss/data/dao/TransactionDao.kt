package com.achmadss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achmadss.data.entities.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNewTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM `Transaction`")
    fun getAllTransactions(): List<Transaction>

}