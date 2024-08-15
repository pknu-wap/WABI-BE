package com.wap.wabi.qr.service

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.qr.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.qr.repository.QrQueryDslRepository
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class QrService (
    val eventRepository: EventRepository,
    val eventStudentRepository: EventStudentRepository,
    val studentRepository: StudentRepository
){
    fun checkIn(checkInRequest: CheckInRequest) { //TODO validate
        val student = studentRepository.findById(checkInRequest.studentId).orElseThrow { throw IllegalArgumentException("no student") }

        val event = eventRepository.findById(checkInRequest.eventId).orElseThrow { throw IllegalArgumentException("no event") }

        val eventStudent = eventStudentRepository.findByStudentAndEvent(student, event).orElseThrow { throw IllegalArgumentException("no student in event") }

        check(eventStudent.status.equals(EventStudentStatus.NOT_CHECK_IN)){throw IllegalStateException("already checked-in")}

        eventStudent.checkIn()

        eventStudentRepository.save(eventStudent)
    }
}