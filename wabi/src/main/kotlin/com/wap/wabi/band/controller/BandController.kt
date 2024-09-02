package com.wap.wabi.band.controller

import com.wap.wabi.band.service.BandService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/bands/")
class BandController(
    private val bandService: BandService
) {
    @GetMapping("{bandId}/students")
    fun getBandStudents(@PathVariable bandId: Long) : ResponseEntity<Response>{
        val response = Response.ok(data = bandService.getBandStudents(bandId = bandId))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
