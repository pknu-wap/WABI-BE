package com.wap.wabi.event.service

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.payload.response.CheckInStatusCount
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.payload.response.EventStudentData
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventStudentRepository: EventStudentRepository,
) {
    fun getCheckInTable (eventId : Long, filter : CheckInTableFilter) : Response{
        val event = eventRepository.findById(eventId).orElseThrow { throw IllegalArgumentException("no event") }

        val eventStudentData = when (filter){
            CheckInTableFilter.ALL -> EventStudentData.of(eventStudentRepository.findAllByEvent(event))
            else -> EventStudentData.of(eventStudentRepository.findAllByEventAndStatus(event,filter.toString()))
        }
        return Response("200", "", eventStudentData)
    }

    fun getCheckInStatus(eventId: Long) : Response{
        val event = eventRepository.findById(eventId).orElseThrow { throw IllegalArgumentException("no event") }

        val checkInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.CHECK_IN.toString())
        val notCheckInCount = eventStudentRepository.getEventStudentStatusCount(event, EventStudentStatus.NOT_CHECK_IN.toString())

        return Response("200", "", CheckInStatusCount(checkInCount, notCheckInCount))
    }




}