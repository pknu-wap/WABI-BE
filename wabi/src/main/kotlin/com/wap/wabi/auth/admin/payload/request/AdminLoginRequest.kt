package com.wap.wabi.auth.admin.payload.request

data class AdminLoginRequest(
    val name: String,
    val password: String,
    val email: String
)
