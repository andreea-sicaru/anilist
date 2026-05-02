package com.aissia.anilist.presentation


// Converts an Int to duration string: eg. 1h 40m
fun Int.toFormattedDuration(): String {
    val h = this / 60
    val m = this % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}