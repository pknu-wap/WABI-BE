package com.wap.wabi.common.payload.response


data class Response(
    val statusCode: String,
    val message: String,
    val data: Any? = null
) {
    companion object {
        private const val OK_STATUS_CODE = "200"
        private const val OK_MESSAGE = "OK"
        fun ok(message: String = OK_MESSAGE, data: Any? = null): Response {
            return Response(
                statusCode = OK_STATUS_CODE,
                message = message,
                data = data
            )
        }
    }
}
