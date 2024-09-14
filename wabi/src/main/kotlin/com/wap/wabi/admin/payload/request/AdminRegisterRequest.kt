package com.wap.wabi.admin.payload.request

data class AdminRegisterRequest(
    val id : String,
    val password: String,
    val bandName: String
)
