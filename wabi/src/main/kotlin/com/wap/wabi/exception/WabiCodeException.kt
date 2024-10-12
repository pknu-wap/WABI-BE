package com.wap.wabi.exception

import com.wap.wabi.exception.payload.response.ErrorReason

open class WabiCodeException(
    protected val errorCode : BaseErrorCode
) : RuntimeException() {
    fun getErrorReason() : ErrorReason = errorCode.getErrorReason()
}