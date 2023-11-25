package com.achmadss.data.database

import android.content.Context
import androidx.room.Room

object LocalDataSourceProvider {

    private lateinit var db: LocalDataSource

    fun initialize(context: Context) {
        db = Room.databaseBuilder(
            context, LocalDataSource::class.java, LocalDataSource.name
        ).fallbackToDestructiveMigration().build()
    }

    fun vehicleDao() = db.vehicleDao()
    fun transactionDao() = db.transactionDao()

}