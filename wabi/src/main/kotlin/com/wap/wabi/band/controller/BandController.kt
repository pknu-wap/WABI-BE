package com.wap.wabi.band.controller

import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.service.BandService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bands/")
class BandController(
    private val bandService: BandService
) {
    @GetMapping("{bandId}/students")
    fun getBandStudents(@PathVariable bandId: Long): ResponseEntity<Response> {
        val response = Response.ok(data = bandService.getBandStudents(bandId = bandId))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("create")
    fun createBand(@RequestParam adminId: Long, @RequestBody request: BandCreateRequest): ResponseEntity<Response> {
        bandService.createBand(adminId = adminId, bandCreateRequest = request)

        val response = Response.ok(message = "success create band")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("{bandId}")
    fun deleteBand(@RequestParam adminId: Long, @PathVariable bandId: Long): ResponseEntity<Response> {
        bandService.deleteBand(adminId = adminId, bandId = bandId)

        val response = Response.ok(message = "success delete band")
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("list")
    fun getBands(@RequestParam adminId: Long): ResponseEntity<Response> {
        val response = Response.ok(data = bandService.getBands(adminId = adminId))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
