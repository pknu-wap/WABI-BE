package com.wap.wabi.event.service

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.payload.response.EventStudentData
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    val eventRepository: EventRepository,
    val eventStudentRepository: EventStudentRepository,
) {
    fun getCheckInTable (eventId : Long, filter : CheckInTableFilter) : Response{
        val event = eventRepository.findById(eventId).orElseThrow { throw IllegalArgumentException("no event") }

        val eventStudentData = when (filter){
            CheckInTableFilter.ALL -> EventStudentData.of(eventStudentRepository.findAllByEvent(event))
            else -> EventStudentData.of(eventStudentRepository.findAllByEventAndStatus(event,filter.toString()))
        }
        return Response("200", "", eventStudentData)
    }




}