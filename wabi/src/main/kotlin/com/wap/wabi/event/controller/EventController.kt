package com.wap.wabi.event.controller

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController (
    private val eventService: EventService
){
    @GetMapping("/{eventId}")
    fun getCheckInTable(@PathVariable("eventId")eventId : Long, @RequestParam filter: CheckInTableFilter) : ResponseEntity<Response> {
        val response = Response.ok(data = eventService.getCheckInTable(eventId = eventId, filter = filter))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{eventId}/status")
    fun getCheckStatus(@PathVariable("eventId") eventId: Long) : ResponseEntity<Response>{
        val response = Response.ok(data = eventService.getCheckInStatus(eventId = eventId))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/check-in/qr")
    fun checkIn(@RequestBody checkInRequest : CheckInRequest) : ResponseEntity<Response> {
        eventService.checkIn(checkInRequest = checkInRequest)

        val response = Response.ok(data = null);
        return ResponseEntity(response, HttpStatus.OK)
    }
}
