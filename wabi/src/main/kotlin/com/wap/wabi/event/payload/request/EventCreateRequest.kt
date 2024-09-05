package com.wap.wabi.event.payload.request

import com.wap.wabi.event.entity.Event
import java.time.LocalDateTime

data class EventCreateRequest(
    val name: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val eventStudentMaxCount: Int,
    val bandIds: List<Long>
) {
    fun toEventEntity(): Event {
        return Event.builder()
            .name(name)
            .startAt(startAt)
            .endAt(endAt)
            .eventStudentMaxCount(eventStudentMaxCount)
            .build()
    }
}