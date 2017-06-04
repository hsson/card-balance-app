// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.controller.MainActivity

object NotificationsHelper {
    fun maybeNotify(context: Context, global: GlobalState) {
        val roundedBalance = Math.round(global.model.cardData.cardBalance).toInt()
        val limit = global.model.notifications.lowBalanceNotificationLimit
        val lastNotifiedBalance = global.model.notifications.lowBalanceLastNotifiedBalance
        val enabled = global.model.notifications.isLowBalanceNotificationsEnabled

        if (enabled && roundedBalance < limit && roundedBalance != lastNotifiedBalance) {
            global.model.notifications.lowBalanceLastNotifiedBalance = roundedBalance
            global.saveNotificationData()
            showLowBalanceNotification(context, roundedBalance)
        }
    }

    fun cancellAll(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    private fun showLowBalanceNotification(context: Context, roundedBalance: Int) {
        val primaryColor = context.getColor(R.color.color_primary)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        val wearableBkg = BitmapFactory.decodeResource(context.resources, R.drawable.bkg_wearable)

        val contentText = context.getString(R.string.notification_text, roundedBalance.toString())

        val wearableExtended = NotificationCompat.WearableExtender().setBackground(wearableBkg)
        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.coin_icon) //TODO: IC STATUS
                .setLargeIcon(largeIcon)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(contentText)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                .setColor(primaryColor)
                .setLights(primaryColor, 1500, 1000)
                .extend(wearableExtended)

        val mainActivityIntent = Intent(context, MainActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(mainActivityIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notificationBuilder.build())
    }
}