package com.rocqjones.mlocker.logic.utils

import java.time.Duration

class HelperUtil {

    // formats duration as HH:MM:SS
    fun formatDuration(duration: Duration): String {
        val seconds = duration.seconds % 60
        val minutes = (duration.seconds / 60) % 60
        val hours = duration.toHours()

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}