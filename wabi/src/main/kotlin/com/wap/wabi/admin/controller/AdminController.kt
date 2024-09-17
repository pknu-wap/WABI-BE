package com.wap.wabi.admin.controller

import com.wap.wabi.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.admin.service.AdminService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admins/")
class AdminController(
    private val adminService: AdminService
) {
    @PostMapping("register")
    fun registerAdmin(@RequestBody request: AdminRegisterRequest): ResponseEntity<Response> {
        adminService.registerAdmin(request)
        val response = Response.ok(message = "success register admin")
        return ResponseEntity(response, HttpStatus.OK)
    }
}