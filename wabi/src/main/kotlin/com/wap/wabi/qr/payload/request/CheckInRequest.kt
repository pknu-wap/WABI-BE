package com.wap.wabi.qr.payload.request

data class CheckInRequest(
    val studentId : String,
    val eventId : Long
) {
}
