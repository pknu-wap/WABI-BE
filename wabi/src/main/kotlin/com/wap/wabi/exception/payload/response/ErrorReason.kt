package com.wap.wabi.exception.payload.response

import org.springframework.http.HttpStatus

data class ErrorReason(
    val status : HttpStatus,
    val code : String,
    val reason : String,
)