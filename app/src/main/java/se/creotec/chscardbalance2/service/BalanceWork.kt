package se.creotec.chscardbalance2.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import se.creotec.chscardbalance2.Constants
import java.util.concurrent.TimeUnit

class BalanceWork (private val context : Context, params : WorkerParameters)
    : Worker(context, params) {

    override fun doWork(): Result {
        Log.i(this::class.java.simpleName, "Starting balance work")

        val updateIntent = Intent(context, BalanceService::class.java)
        updateIntent.action = Constants.ACTION_UPDATE_CARD
        context.startService(updateIntent)

        return Result.success()
    }

    companion object {

        private const val WORK_TAG = "balance_work"

        fun scheduleRepeating() {
            val constraint = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val work = PeriodicWorkRequestBuilder<BalanceWork>(30, TimeUnit.MINUTES)
                    .setConstraints(constraint)
                    .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, work)
        }
    }
}