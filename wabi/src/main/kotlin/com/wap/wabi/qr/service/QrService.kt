package com.wap.wabi.qr.service

import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.qr.payload.request.CheckInRequest
import com.wap.wabi.event.repository.EventStudentRepository
import org.springframework.stereotype.Service
import com.wap.wabi.student.repository.StudentRepository

@Service
class QrService (
    val eventRepository: EventRepository,
    val studentRepository: StudentRepository,
    val eventStudentRepository: EventStudentRepository
){
    fun checkIn(checkInRequest: CheckInRequest) {
        //student repository 부터 확인
    }
}