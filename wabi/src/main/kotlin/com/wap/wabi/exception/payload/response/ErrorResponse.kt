package com.wap.wabi.exception.payload.response

data class ErrorResponse (
    val errorReason: ErrorReason,
    val path: String
)