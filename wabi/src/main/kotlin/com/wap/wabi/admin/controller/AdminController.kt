package com.wap.wabi.admin.controller

import com.wap.wabi.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.admin.service.AdminService
import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admins/")
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping("/register")
    fun registerAdmin(@RequestParam("adminData") request: AdminRegisterRequest): ResponseEntity<Response> {
        val response = Response.ok(data = adminService.registerAdmin(request))
        return ResponseEntity(response, HttpStatus.OK)
    }
}