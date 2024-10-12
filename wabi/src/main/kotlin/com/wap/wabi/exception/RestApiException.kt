package com.wap.wabi.exception


class RestApiException(val globalErrorCode: GlobalErrorCode) : RuntimeException()
