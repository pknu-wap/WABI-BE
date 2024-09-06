package com.wap.wabi.event.payload.request

import com.wap.wabi.event.entity.Event
import java.time.LocalDateTime

data class EventCreateRequest(
    val eventName: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val eventStudentMaxCount: Int,
    val bandIds: List<Long>
) {
    fun toEventEntity(adminId: Long): Event {
        return Event.builder()
            .adminId(adminId)
            .name(eventName)
            .startAt(startAt)
            .endAt(endAt)
            .eventStudentMaxCount(eventStudentMaxCount)
            .build()
    }
}
