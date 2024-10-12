package com.wap.wabi.exception

import com.wap.wabi.exception.payload.response.ErrorReason

interface BaseErrorCode {
    fun getErrorReason(): ErrorReason
}