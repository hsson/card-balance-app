// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName
import java.util.*

class OpenHour {
    @SerializedName("day_of_week")
    var dayOfWeek: Int = 2
        set(value) {
            if (value in 1..7) {
                field = value
            }
        }
    @SerializedName("start_hour")
    var startHour: Int = 1
        set(value) {
            if (value in 0..2400) {
                field = value
            }
        }
    @SerializedName("end_hour")
    var endHour: Int = 2400
        set(value) {
            if (value in 0..2400) {
                field = value
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as OpenHour

        if (dayOfWeek != other.dayOfWeek) return false
        if (startHour != other.startHour) return false
        if (endHour != other.endHour) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dayOfWeek
        result = 31 * result + startHour
        result = 31 * result + endHour
        return result
    }

    override fun toString(): String {
        return "OpenHour(dayOfWeek=$dayOfWeek, startHour=$startHour, endHour=$endHour)"
    }

    companion object {
        fun toUnixTimeStamp(time: Int): Long {
            if (time !in 0..2400) {
                return -1
            }
            val c = Calendar.getInstance()
            c.time = Date()
            val timeStr = time.toString()
            if (timeStr.length <= 2) {
                c.set(Calendar.HOUR_OF_DAY, 0)
                c.set(Calendar.MINUTE, time)
                return c.timeInMillis

            } else if (timeStr.length == 3) {
                c.set(Calendar.HOUR_OF_DAY, time/100)
                c.set(Calendar.MINUTE, timeStr.substring(1).toInt())
                return c.timeInMillis
            } else {
                c.set(Calendar.HOUR_OF_DAY, time/100)
                c.set(Calendar.MINUTE, timeStr.substring(2).toInt())
                return c.timeInMillis
            }
        }
    }
}