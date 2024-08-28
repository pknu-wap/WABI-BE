package com.wap.wabi.qr.service

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.qr.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.qr.repository.QrQueryDslRepository
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class QrService (
    val eventRepository: EventRepository,
    val eventStudentRepository: EventStudentRepository,
    val studentRepository: StudentRepository
){
    fun checkIn(checkInRequest: CheckInRequest) {
        val student = studentRepository.findById(checkInRequest.studentId).orElseThrow { throw RestApiException(ErrorCode.BAD_REQUEST_STUDENT) }

        val event = eventRepository.findById(checkInRequest.eventId).orElseThrow { throw RestApiException(ErrorCode.BAD_REQUEST_EVENT) }

        val eventStudent = eventStudentRepository.findByStudentAndEvent(student, event).orElseThrow { throw RestApiException(ErrorCode.UNAUTHORIZED_CHECK_IN) }

        check(eventStudent.status.equals(EventStudentStatus.NOT_CHECK_IN)){throw RestApiException(ErrorCode.ALREADY_CHECK_IN)}

        eventStudent.checkIn()

        eventStudentRepository.save(eventStudent)
    }
}