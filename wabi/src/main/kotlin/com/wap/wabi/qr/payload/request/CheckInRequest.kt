package com.wap.wabi.qr.payload.request

data class CheckInRequest(
    val studientId : String,
    val eventId : Long
) {
}