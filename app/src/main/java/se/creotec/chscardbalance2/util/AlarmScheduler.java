// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public final class AlarmScheduler {

    private AlarmScheduler() {
        super();
    }

    private static final String LOG_TAG = AlarmScheduler.class.getName();

    /**
     * Schedule an intent to be run every hour. Use the flag
     *
     * @param context Context to get alarm service from
     * @param intent The intent to schedule
     * @param flag Any flag for the alarm scheduler
     */
    public synchronized static void scheduleAlarm(Context context, Intent intent, int flag) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, flag);
        if (alarmIntent != null) {
            Log.d(LOG_TAG, "Scheduling alarm for intent " + intent.toString());
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_HOUR,
                    alarmIntent);
        }
    }

    /**
     * Check if an alarm is scheduled for the given Intent
     *
     * @param context Context to get alarm service from
     * @param intent The intent to check
     * @return True if the intent is already scheduled, false otherwise
     */
    public static boolean isAlarmExistingForIntent(Context context, Intent intent) {
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }
}
