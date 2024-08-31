package com.wap.wabi.band.controller

import com.wap.wabi.band.payload.request.EnrollRequest
import com.wap.wabi.band.service.BandEnrollService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/api/bands/")
class BandEnrollController (
    private val bandEnrollService: BandEnrollService
){
    @PostMapping("{bandId}/members/enrollments/file")
    fun enrollByFile(@PathVariable bandId: Long, @RequestParam("file") file : MultipartFile) : ResponseEntity<Response> {
        return ResponseEntity(bandEnrollService.enrollByFile(bandId = bandId, file = file), HttpStatus.OK)
    }

    @PostMapping("{bandId}/members/enrollments/manual")
    fun enrollByManual(@PathVariable bandId: Long, @RequestBody request : EnrollRequest) : ResponseEntity<Response> {
        return ResponseEntity(bandEnrollService.enrollBandStudent(bandId = bandId, request = request), HttpStatus.OK)
    }
}
