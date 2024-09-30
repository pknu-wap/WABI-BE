package com.wap.wabi.event.payload.response

import com.wap.wabi.band.payload.response.BandShortData
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventBand
import java.time.LocalDateTime

data class EventData(
    val eventId: Long,
    val eventName: String,
    val eventStudentMaxCount: Int,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val bands: List<BandShortData>,
    val checkInStatusCount: CheckInStatusCount
) {
    companion object {
        fun of(event: Event, eventBands: List<EventBand>, checkInStatusCount: CheckInStatusCount): EventData {
            val bands = eventBands.map { it.band }
            return EventData(
                eventId = event.id,
                eventName = event.name,
                eventStudentMaxCount = event.eventStudentMaxCount,
                startAt = event.startAt,
                endAt = event.endAt,
                bands = BandShortData.of(bands),
                checkInStatusCount = checkInStatusCount
            )
        }
    }
}
