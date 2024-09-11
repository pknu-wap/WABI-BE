package com.wap.wabi.event.controller

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.service.EventService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {
    @GetMapping("/check-in/{eventId}")
    @Operation(
        summary = "이벤트 체크인 명단 조회",
        description = "이벤트 관련 명단을 불러옵니다.\n" +
                "ALL(전부), NOT_CHECKIN(체크인 안한 인원), CHECK_IN(체크인 한 인원)"
    )
    fun getCheckInTable(
        @PathVariable("eventId") eventId: Long,
        @RequestParam filter: CheckInTableFilter
    ): ResponseEntity<Response> {
        val response = Response.ok(data = eventService.getCheckInTable(eventId = eventId, filter = filter))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{eventId}/status")
    @Operation(summary = "이벤트 체크인 인원수 조회", description = "체크인한 인원수와 체크인 안한 인원수 조회")
    fun getCheckStatus(@PathVariable("eventId") eventId: Long): ResponseEntity<Response> {
        val response = Response.ok(data = eventService.getCheckInStatus(eventId = eventId))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/check-in")
    @Operation(summary = "체크인")
    fun checkIn(@RequestBody checkInRequest: CheckInRequest): ResponseEntity<Response> {
        eventService.checkIn(checkInRequest = checkInRequest)

        val response = Response.ok()
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping
    @Operation(summary = "이벤트 생성")
    fun createEvent(
        @RequestParam adminId: Long,
        @RequestBody request: EventCreateRequest
    ): ResponseEntity<Response> {
        eventService.createEvent(adminId = adminId, eventCreateRequest = request)

        val response = Response.ok(message = "success create event")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("")
    @Operation(summary = "이벤트 수정")
    fun updateEvent(
        @RequestParam adminId: Long,
        @RequestBody request: EventUpdateRequest
    ): ResponseEntity<Response> {
        eventService.updateEvent(adminId = adminId, eventUpdateRequest = request)

        val response = Response.ok(message = "success update event")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "이벤트 조회")
    fun getEvent(
        @PathVariable("eventId") eventId: Long,
        @RequestParam adminId: Long
    ): ResponseEntity<Response> {
        val result = eventService.getEvent(adminId = adminId, eventId = eventId)

        val response = Response.ok(data = result)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/list")
    @Operation(summary = "이벤트 목록 조회")
    fun getEvents(
        @RequestParam adminId: Long
    ): ResponseEntity<Response> {
        val result = eventService.getEvents(adminId = adminId)

        val response = Response.ok(data = result)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "이벤트 삭제")
    fun deleteEvent(
        @PathVariable("eventId") eventId: Long,
        @RequestParam adminId: Long
    ): ResponseEntity<Response> {
        eventService.deleteEvent(adminId = adminId, eventId = eventId)

        val response = Response.ok(message = "success delete event")
        return ResponseEntity(response, HttpStatus.OK)
    }
}
