package com.wap.wabi.band.controller

import com.wap.wabi.band.payload.request.EnrollRequest
import com.wap.wabi.band.service.BandEnrollService
import com.wap.wabi.common.payload.response.Response
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/bands/")
class BandEnrollController(
    private val bandEnrollService: BandEnrollService
) {
    @PostMapping("{bandId}/members/enrollments/file")
    @Operation(
        summary = "밴드 학생 추가(excel, csv)"
    )
    fun enrollByFile(
        @PathVariable bandId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Response> {
        val response = Response.ok(data = "bandId : " + bandEnrollService.enrollByFile(bandId = bandId, file = file))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("{bandId}/members/enrollments/manual")
    @Operation(
        summary = "밴드 학생 추가",
        description = "밴드에 학생들을 추가 가능합니다."
    )
    fun enrollByManual(@PathVariable bandId: Long, @RequestBody request: EnrollRequest): ResponseEntity<Response> {
        val response =
            Response.ok(data = "bandId : " + bandEnrollService.enrollBandStudent(bandId = bandId, request = request))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
