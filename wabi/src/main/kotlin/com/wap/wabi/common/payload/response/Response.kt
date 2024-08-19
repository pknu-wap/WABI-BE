package com.wap.wabi.common.payload.response

import java.util.Objects

data class Response(
    val statusCode : String,
    val message : String,
    val data : Any
)
