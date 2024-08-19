package com.wap.wabi.qr.controller

import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.qr.payload.request.CheckInRequest
import com.wap.wabi.qr.service.QrService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/qr")
class QrController (
    val qrService : QrService
){
    @GetMapping("/check-in")
    fun checkIn(@RequestBody checkInRequest: CheckInRequest) : ResponseEntity<String>{
        qrService.checkIn(checkInRequest);
        return ResponseEntity.ok("success check-in")
    }
}