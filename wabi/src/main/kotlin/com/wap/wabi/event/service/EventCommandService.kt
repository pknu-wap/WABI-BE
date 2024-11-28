package com.wap.wabi.event.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventBand
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.event.entity.EventStudentBandName
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentBandNameRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EventCommandService(
    private val eventRepository: EventRepository,
    private val eventStudentRepository: EventStudentRepository,
    private val studentRepository: StudentRepository,
    private val eventBandRepository: EventBandRepository,
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository,
    private val eventStudentBandNameRepository: EventStudentBandNameRepository
) {
    @Transactional
    fun checkIn(checkInRequest: CheckInRequest): EventStudentStatus {
        //TODO 해당 이벤트에 체크인할 권한이 있는지 검증 필요.

        val student = studentRepository.findById(checkInRequest.studentId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_STUDENT) }
        val event = eventRepository.findById(checkInRequest.eventId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }
        val eventStudent = eventStudentRepository.findByStudentAndEvent(student, event)
            .orElseThrow { RestApiException(ErrorCode.UNAUTHORIZED_CHECK_IN) }

        if (eventStudent.status.equals(EventStudentStatus.CHECK_IN)) {
            throw RestApiException(ErrorCode.ALREADY_CHECK_IN)
        }
        return eventStudent.checkIn()

    }

    @Transactional
    fun createEvent(adminId: Long, eventCreateRequest: EventCreateRequest): Event {
        val createdEvent = eventCreateRequest.toEventEntity(adminId)
        val savedEvent = eventRepository.save(createdEvent)

        val bands = bandRepository.findAllById(eventCreateRequest.bandIds)
        if (bands.size != eventCreateRequest.bandIds.size) {
            throw RestApiException(ErrorCode.NOT_FOUND_BAND)
        }

        val eventBands = bands.map { band ->
            EventBand.builder()
                .event(savedEvent)
                .band(band)
                .build()
        }
        eventBandRepository.saveAll(eventBands)

        bands.forEach { band ->
            saveEventStudentsFromBand(savedEvent, band)
        }
        return savedEvent
    }

    @Transactional
    fun saveEventStudentsFromBand(event: Event, band: Band): Long {
        bandStudentRepository.findAllByBand(band).forEach { bandStudent ->
            val eventStudent: EventStudent
            val eventStudentOptional = eventStudentRepository.findByStudentAndEvent(bandStudent.student, event)
            if (eventStudentOptional.isEmpty) {
                val newEventStudent = EventStudent.builder()
                    .event(event)
                    .student(bandStudent.student)
                    .build()

                eventStudent = eventStudentRepository.save(newEventStudent)
            } else {
                eventStudent = eventStudentOptional.get()
            }

            val eventStudentBandName = EventStudentBandName.builder()
                .eventStudent(eventStudent)
                .bandName(band.bandName)
                .build()

            eventStudentBandNameRepository.save(eventStudentBandName)
        }
        return event.id
    }

    @Transactional
    fun updateEvent(adminId: Long, eventUpdateRequest: EventUpdateRequest): Event {
        val event = eventRepository.findById(eventUpdateRequest.eventId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        validateEventOwner(adminId, event)

        try {
            event.update(eventUpdateRequest)
        } catch (e: IllegalArgumentException) {
            throw RestApiException(ErrorCode.FORBIDDEN_ACCESS_EVENT_UPDATE)
        }

        return event
    }

    @Transactional
    fun deleteEvent(adminId: Long, eventId: Long) {
        val event =
            eventRepository.findById(eventId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_EVENT) }

        validateEventOwner(adminId, event)

        val eventStudens = eventStudentRepository.findAllByEvent(event)

        eventStudens.forEach { eventStudent ->
            eventStudentBandNameRepository.deleteByEventStudent(eventStudent)
        }

        eventStudentRepository.deleteByEvent(event)
        eventBandRepository.deleteByEvent(event)
        eventRepository.delete(event)
    }

    private fun validateEventOwner(adminId: Long, event: Event): Boolean {
        if (!event.isOwner(adminId)) throw RestApiException(ErrorCode.UNAUTHORIZED_EVENT)
        return true
    }
}