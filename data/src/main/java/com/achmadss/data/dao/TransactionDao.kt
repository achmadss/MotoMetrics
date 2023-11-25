package com.achmadss.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.achmadss.data.entities.Transaction

@Dao
interface TransactionDao {

    @Upsert
    fun upsertTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM `Transaction`")
    fun getAllTransactions(): List<Transaction>

}