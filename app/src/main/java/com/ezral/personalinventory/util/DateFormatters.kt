package com.ezral.personalinventory.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val displayDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

fun formatExpiryDate(millis: Long?): String? =
    millis?.let { displayDate.format(Date(it)) }
