package com.achmadss.motometrics.crash

import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.system.exitProcess

class GlobalExceptionHandler private constructor(
    private val applicationContext: Context,
    private val activityToBeLaunched: Class<*>
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            exception.printStackTrace()
            launchActivity(applicationContext, activityToBeLaunched, exception)
            exitProcess(0)
        } catch (e: Exception) {
            Log.e("GlobalExceptionHandler", e.message ?: "Error handling uncaught exception")
        }
    }

    private fun launchActivity(
        applicationContext: Context,
        activity: Class<*>,
        exception: Throwable,
    ) {
        val intent = Intent(applicationContext, activity).apply {
            putExtra(INTENT_EXTRA, exception.stackTraceToString())
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        applicationContext.startActivity(intent)
    }

    companion object {
        private const val INTENT_EXTRA = "Throwable"

        fun initialize(
            applicationContext: Context,
            activityToBeLaunched: Class<*>,
        ) {
            val handler = GlobalExceptionHandler(applicationContext, activityToBeLaunched)
            Thread.setDefaultUncaughtExceptionHandler(handler)
        }

        fun getThrowableFromIntent(intent: Intent): Throwable? {
            return try {
                val throwableString = intent.getStringExtra(INTENT_EXTRA)
                Throwable(throwableString)
            } catch (e: Exception) {
                Log.e("GlobalExceptionHandler", e.message ?: "Error retrieving throwable from intent")
                null
            }
        }
    }
}
