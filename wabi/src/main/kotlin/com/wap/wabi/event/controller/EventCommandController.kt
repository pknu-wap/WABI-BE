package com.wap.wabi.event.controller

import com.wap.wabi.auth.admin.service.AdminService
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.service.EventCommandService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventCommandController(
    private val eventCommandService: EventCommandService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val adminService: AdminService
) {
    @PostMapping
    @Operation(summary = "이벤트 생성")
    fun createEvent(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: EventCreateRequest
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        eventCommandService.createEvent(adminId = adminId, eventCreateRequest = request)

        val response = Response.ok(message = "success create event")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("")
    @Operation(summary = "이벤트 수정")
    fun updateEvent(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: EventUpdateRequest
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        eventCommandService.updateEvent(adminId = adminId, eventUpdateRequest = request)

        val response = Response.ok(message = "success update event")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "이벤트 삭제")
    fun deleteEvent(
        @PathVariable("eventId") eventId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        eventCommandService.deleteEvent(adminId = adminId, eventId = eventId)

        val response = Response.ok(message = "success delete event")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/check-in")
    @Operation(summary = "체크인")
    fun checkIn(@RequestBody checkInRequest: CheckInRequest): ResponseEntity<Response> {
        eventCommandService.checkIn(checkInRequest = checkInRequest)

        val response = Response.ok()
        return ResponseEntity(response, HttpStatus.OK)
    }
}