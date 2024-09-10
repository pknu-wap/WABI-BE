package com.wap.wabi.exception


class RestApiException(val errorCode: ErrorCode) : RuntimeException()
