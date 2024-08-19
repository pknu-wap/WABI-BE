package com.wap.wabi.event.controller

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController (
    val eventService: EventService
){
    @GetMapping("/{eventId}")
    fun getCheckInTable(@PathVariable("eventId")eventId : Long, @RequestParam filter: CheckInTableFilter) : ResponseEntity<Response> {
        return ResponseEntity(eventService.getCheckInTable(eventId, filter), HttpStatus.OK)
    }
}