package com.wap.wabi.band.controller

import com.wap.wabi.band.payload.request.EnrollRequest
import com.wap.wabi.band.service.BandEnrollService
import com.wap.wabi.common.payload.response.Response
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
    fun enrollByFile(
        @PathVariable bandId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Response> {
        val response = Response.ok(data = "bandId : " + bandEnrollService.enrollByFile(bandId = bandId, file = file))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("{bandId}/members/enrollments/manual")
    fun enrollByManual(@PathVariable bandId: Long, @RequestBody request: EnrollRequest): ResponseEntity<Response> {
        val response =
            Response.ok(data = "bandId : " + bandEnrollService.enrollBandStudent(bandId = bandId, request = request))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
