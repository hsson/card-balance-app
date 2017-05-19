// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.service.BalanceService;
import se.creotec.chscardbalance2.util.AlarmScheduler;

public class BootReceiver extends BroadcastReceiver{

    private static final String LOG_TAG = BootReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "System boot detected");
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(Constants.INSTANCE.getACTION_BOOT_COMPLETED())) {
            Intent updateCardIntent = new Intent(context, BalanceService.class);
            updateCardIntent.setAction(Constants.INSTANCE.getACTION_UPDATE_CARD());
            Log.d(LOG_TAG, "Scheduling card update after boot");
            AlarmScheduler.scheduleAlarm(context, updateCardIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }
}
