package com.wap.wabi.event.service

import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventBand
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.payload.response.CheckInStatusCount
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.payload.response.EventData
import com.wap.wabi.event.payload.response.EventStudentData
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventStudentRepository: EventStudentRepository,
    private val studentRepository: StudentRepository,
    private val eventBandRepository: EventBandRepository,
    private val bandRepository: BandRepository
) {
    @Transactional
    fun getCheckInTable(eventId: Long, filter: CheckInTableFilter): List<EventStudentData> {
        val event = eventRepository.findById(eventId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        val eventStudentData = when (filter) {
            CheckInTableFilter.ALL -> EventStudentData.of(eventStudentRepository.findAllByEvent(event))
            else -> EventStudentData.of(eventStudentRepository.findAllByEventAndStatus(event, filter.toString()))
        }
        return eventStudentData
    }

    @Transactional
    fun getCheckInStatus(eventId: Long): CheckInStatusCount {
        val event = eventRepository.findById(eventId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        val checkInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.CHECK_IN)
        val notCheckInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.NOT_CHECK_IN)

        return CheckInStatusCount(checkInCount, notCheckInCount)
    }

    @Transactional
    fun checkIn(checkInRequest: CheckInRequest) {
        val student = studentRepository.findById(checkInRequest.studentId)
            .orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_STUDENT) }
        val event = eventRepository.findById(checkInRequest.eventId)
            .orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_EVENT) }
        val eventStudent = eventStudentRepository.findByStudentAndEvent(student, event)
            .orElseThrow { throw RestApiException(ErrorCode.UNAUTHORIZED_CHECK_IN) }

        check(eventStudent.status.equals(EventStudentStatus.NOT_CHECK_IN)) { throw RestApiException(ErrorCode.ALREADY_CHECK_IN) }
        eventStudent.checkIn()

        eventStudentRepository.save(eventStudent)
    }

    @Transactional
    fun createEvent(adminId: Long, eventCreateRequest: EventCreateRequest): Event {
        val createdEvent = eventCreateRequest.toEventEntity(adminId)
        val savedEvent = eventRepository.save(createdEvent)

        val bands = bandRepository.findAllById(eventCreateRequest.bandIds)
        check(bands.size == eventCreateRequest.bandIds.size) { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

        val eventBands = bands.map { band ->
            EventBand.builder()
                .event(savedEvent)
                .band(band)
                .build()
        }
        eventBandRepository.saveAll(eventBands)

        return savedEvent
    }

    @Transactional
    fun updateEvent(adminId: Long, eventUpdateRequest: EventUpdateRequest): Event {
        val event = eventRepository.findById(eventUpdateRequest.eventId)
            .orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        validateEventOwner(adminId, event)

        try {
            event.update(eventUpdateRequest)
        } catch (e: IllegalArgumentException) {
            throw RestApiException(ErrorCode.FORBIDDEN_ACCESS_EVENT_UPDATE)
        }

        return event
    }

    fun getEvent(adminId: Long, eventId: Long): EventData {
        val event =
            eventRepository.findById(eventId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        validateEventOwner(adminId, event)

        val eventBands = eventBandRepository.findAllByEvent(event)

        return EventData.of(event, eventBands)
    }

    fun getEvents(adminId: Long): List<EventData> {
        val events = eventRepository.findAllByAdminId(adminId)

        val eventDatas: MutableList<EventData> = ArrayList()
        events.forEach { event ->
            val eventBands = eventBandRepository.findAllByEvent(event)
            eventDatas.add(EventData.of(event, eventBands))
        }

        return eventDatas
    }

    fun validateEventOwner(adminId: Long, event: Event) {
        if (!event.isOwner(adminId)) throw RestApiException(ErrorCode.UNAUTHORIZED_EVENT)
    }
}
