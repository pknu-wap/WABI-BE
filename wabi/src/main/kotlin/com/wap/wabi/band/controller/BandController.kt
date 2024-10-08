package com.wap.wabi.band.controller

import com.wap.wabi.auth.admin.service.AdminService
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.payload.request.BandUpdateRequest
import com.wap.wabi.band.payload.response.BandStudentsData
import com.wap.wabi.band.service.BandService
import com.wap.wabi.common.payload.response.Response
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bands/")
class BandController(
    private val bandService: BandService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val adminService: AdminService
) {
    @GetMapping("{bandId}/students")
    @Operation(
        summary = "밴드에 속한 학생 명단 조회"
    )
    fun getBandStudents(@PathVariable bandId: Long): ResponseEntity<Response> {
        val response = Response.ok(data = BandStudentsData(bandService.getBandStudents(bandId = bandId)))

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("create")
    @Operation(
        summary = "밴드 생성"
    )
    fun createBand(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: BandCreateRequest
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        bandService.createBand(adminId = adminId, bandCreateRequest = request)

        val response = Response.ok(message = "success create band")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("{bandId}")
    @Operation(
        summary = "밴드 삭제"
    )
    fun deleteBand(
        @RequestHeader("Authorization") token: String,
        @PathVariable bandId: Long
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        bandService.deleteBand(adminId = adminId, bandId = bandId)

        val response = Response.ok(message = "success delete band")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("list")
    @Operation(
        summary = "해당 계정의 밴드 목록을 불러옵니다."
    )
    fun getBands(@RequestHeader("Authorization") token: String): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        val response = Response.ok(data = bandService.getBands(adminId = adminId))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("")
    @Operation(
        summary = "밴드 수정"
    )
    fun updateBand(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: BandUpdateRequest
    ): ResponseEntity<Response> {
        val adminName = jwtTokenProvider.getAdminNameByToken(token.removePrefix("Bearer "))
        val adminId = adminService.getAdminId(adminName = adminName)
        bandService.updateBand(adminId = adminId, bandUpdateRequest = request)

        val response = Response.ok(message = "success update band")

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("{bandId}/detail")
    @Operation(
        summary = "밴드 상세 정보 조회"
    )
    fun getBandDetail(@PathVariable bandId: Long): ResponseEntity<Response> {

        val response = Response.ok(data = bandService.getBandDetail(bandId = bandId))

        return ResponseEntity(response, HttpStatus.OK)
    }
}
