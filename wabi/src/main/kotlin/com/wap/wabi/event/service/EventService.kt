package com.wap.wabi.event.service

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.response.CheckInStatusCount
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.payload.response.EventStudentData
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
    private val studentRepository: StudentRepository
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
    }
}
