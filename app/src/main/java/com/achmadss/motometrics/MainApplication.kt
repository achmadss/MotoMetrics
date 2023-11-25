package com.achmadss.motometrics

import android.app.Application
import com.achmadss.data.database.LocalDataSourceProvider
import com.achmadss.motometrics.crash.CrashActivity
import com.achmadss.motometrics.crash.GlobalExceptionHandler

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalExceptionHandler.initialize(this, CrashActivity::class.java)
        LocalDataSourceProvider.initialize(this)
    }

}