package com.wap.wabi.common.payload.response

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class Response(
    val statusCode : String,
    val message : String,
    val data : Any?
){
    companion object {
        private const val OK_STATUS_CODE = "200"
        private const val OK_MESSAGE = "OK"
        fun ok(data: Any?): Response {
            return Response(
                statusCode = OK_STATUS_CODE,
                message = OK_MESSAGE,
                data = data
            )
        }
    }
}
