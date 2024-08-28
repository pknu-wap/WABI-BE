package com.wap.wabi.exception

import com.wap.wabi.common.payload.response.Response

class RestApiException(val errorCode : ErrorCode) : RuntimeException()
