package com.wap.wabi.band.controller

import com.wap.wabi.band.payload.request.ManualEnrollRequest
import com.wap.wabi.band.service.BandService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/api/bands/")
class BandController(
    private val bandService: BandService
) {
    @GetMapping("{bandId}/students")
    fun getBandStudents(@PathVariable bandId: Long) : ResponseEntity<Response>{
        return ResponseEntity(bandService.getBandStudents(bandId = bandId), HttpStatus.OK)
    }

    @PostMapping("{bandId}/members/enrollments/file")
    fun enrollByFile(@PathVariable bandId: Long, @RequestParam("file") file : MultipartFile) : ResponseEntity<Response>{
        return ResponseEntity(bandService.enrollByFile(bandId, file), HttpStatus.OK)
    }

    @PostMapping("/{bandId}/members/enrollments/manual")
    fun enrollByManual(@PathVariable bandId: Long, @RequestBody request : ManualEnrollRequest) : ResponseEntity<Response>{
        return ResponseEntity(bandService.enrollByManual(bandId, request), HttpStatus.OK)
    }
}