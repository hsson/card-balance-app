// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.util.AlarmScheduler

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(LOG_TAG, "System boot detected")
        if (intent == null || intent.action == null) {
            return
        } else if (intent.action == Constants.ACTION_BOOT_COMPLETED) {
            val updateCardIntent = Intent(context, BalanceService::class.java)
            updateCardIntent.action = Constants.ACTION_UPDATE_CARD
            Log.d(LOG_TAG, "Scheduling card update after boot")
            AlarmScheduler.scheduleAlarm(context, updateCardIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }

    companion object {
        private val LOG_TAG = BootReceiver::class.java.name
    }
}
