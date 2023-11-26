package com.achmadss.motometrics.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatPattern(pattern: String): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}