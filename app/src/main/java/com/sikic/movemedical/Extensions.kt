package com.sikic.movemedical

import com.google.android.material.timepicker.MaterialTimePicker
import java.util.Locale

const val HOURS_IN_12_FORMAT = 12

fun MaterialTimePicker.prepareTimeInReadableFormat(): String {
    val hoursIn12HourFormat = if (this.hour > HOURS_IN_12_FORMAT) this.hour - HOURS_IN_12_FORMAT else this.hour
    val minute = this.minute
    val period = if (this.hour >= HOURS_IN_12_FORMAT) "PM" else "AM"
    return String.format(Locale.US, "%02d:%02d %s", hoursIn12HourFormat, minute, period)
}