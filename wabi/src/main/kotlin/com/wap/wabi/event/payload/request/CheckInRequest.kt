package com.wap.wabi.event.payload.request

data class CheckInRequest(
    val studentId: String,
    val eventId: Long
) {
}
