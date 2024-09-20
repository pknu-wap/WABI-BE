package com.wap.wabi.event.payload.request

import java.time.LocalDateTime

data class EventUpdateRequest(
    val eventId: Long,
    val eventName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val eventStudentMaxCount: Int,
)
