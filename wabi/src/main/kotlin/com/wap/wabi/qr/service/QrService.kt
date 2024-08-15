package com.wap.wabi.qr.service

import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.qr.payload.request.CheckInRequest
import org.springframework.stereotype.Service

@Service
class QrService (
    val eventRepository: EventRepository,

){
    fun checkIn(checkInRequest: CheckInRequest) {
    }
}