package com.melardev.android.crud.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.longTimeToStr(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return format.format(date)
}