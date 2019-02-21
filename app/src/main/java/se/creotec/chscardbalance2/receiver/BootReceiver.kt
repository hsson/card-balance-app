// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.service.BalanceWork

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(LOG_TAG, "System boot detected")
        if (intent == null || intent.action == null) {
            return
        } else if (intent.action == Constants.ACTION_BOOT_COMPLETED) {
            BalanceWork.scheduleRepeating()
        }
    }

    companion object {
        private val LOG_TAG = BootReceiver::class.java.name
    }
}
