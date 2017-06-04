// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName
import se.creotec.chscardbalance2.Constants

class NotificationData {

    companion object { val NEVER_NOTIFIED: Int = -1 }

    @SerializedName("is_low_balance_notifications_enabled")
    var isLowBalanceNotificationsEnabled: Boolean = Constants.PREFS_NOTIFICATION_LOW_BALANCE_ENABLED_DEFAULT
    @SerializedName("low_balance_notification_limit")
    var lowBalanceNotificationLimit: Int = Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_DEFAULT
    @SerializedName("low_balance_last_notified_balance")
    var lowBalanceLastNotifiedBalance: Int = NEVER_NOTIFIED
}