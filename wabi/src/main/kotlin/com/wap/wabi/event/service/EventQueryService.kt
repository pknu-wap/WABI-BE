package com.wap.wabi.event.service

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.event.payload.response.CheckInStatusCount
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.payload.response.EventData
import com.wap.wabi.event.payload.response.EventStudentData
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentBandNameRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EventQueryService(
    private val eventRepository: EventRepository,
    private val eventStudentRepository: EventStudentRepository,
    private val eventBandRepository: EventBandRepository,
    private val eventStudentBandNameRepository: EventStudentBandNameRepository
) {

    @Transactional
    fun getCheckInTable(eventId: Long, filter: CheckInTableFilter): List<EventStudentData> {
        //TODO 이벤트 명단 확인을 위한 권한 확인 필요

        val event = eventRepository.findById(eventId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        val eventStudents: List<EventStudent>
        if (filter == CheckInTableFilter.ALL) {
            eventStudents = eventStudentRepository.findAllByEvent(event)
        } else {
            eventStudents = eventStudentRepository.findAllByEventAndStatus(
                event,
                EventStudentStatus.valueOf(filter.toString())
            )
        }

        val eventStudentDatas = eventStudents.map { eventStudent ->
            val eventStudentBandNames = eventStudentBandNameRepository.findAllByEventStudent(eventStudent)
            EventStudentData.of(eventStudent, eventStudentBandNames)
        }

        return eventStudentDatas
    }

    @Transactional
    fun getEvent(adminId: Long, eventId: Long): EventData {
        val event =
            eventRepository.findById(eventId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        validateEventOwner(adminId, event)

        val eventBands = eventBandRepository.findAllByEvent(event)

        return EventData.of(event, eventBands, getCheckInStatus(eventId))
    }

    fun validateEventOwner(adminId: Long, event: Event): Boolean {
        if (!event.isOwner(adminId)) throw RestApiException(ErrorCode.UNAUTHORIZED_EVENT)
        return true
    }

    @Transactional
    fun getCheckInStatus(eventId: Long): CheckInStatusCount {
        val event = eventRepository.findById(eventId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        val checkInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.CHECK_IN)
        val notCheckInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.NOT_CHECK_IN)

        return CheckInStatusCount(checkInCount, notCheckInCount)
    }

    @Transactional
    fun getEvents(adminId: Long): List<EventData> {
        val events = eventRepository.findAllByAdminId(adminId)

        val eventDatas: MutableList<EventData> = ArrayList()
        events.forEach { event ->
            val eventBands = eventBandRepository.findAllByEvent(event)
            eventDatas.add(EventData.of(event, eventBands, getCheckInStatus(event.id)))
        }

        return eventDatas
    }
}