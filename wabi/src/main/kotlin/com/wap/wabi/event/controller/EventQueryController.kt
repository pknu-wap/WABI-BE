package com.wap.wabi.event.controller

import com.wap.wabi.auth.admin.service.AdminService
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.payload.response.Enum.CheckInTableFilter
import com.wap.wabi.event.service.EventQueryService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventQueryController(
    private val eventQueryService: EventQueryService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val adminService: AdminService
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
        val response = Response.ok(data = eventQueryService.getCheckInTable(eventId = eventId, filter = filter))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "이벤트 조회")
    fun getEvent(
        @PathVariable("eventId") eventId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        val result = eventQueryService.getEvent(adminId = adminId, eventId = eventId)

        val response = Response.ok(data = result)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/list")
    @Operation(summary = "이벤트 목록 조회")
    fun getEvents(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        val result = eventQueryService.getEvents(adminId = adminId)

        val response = Response.ok(data = result)
        return ResponseEntity(response, HttpStatus.OK)
    }

}
