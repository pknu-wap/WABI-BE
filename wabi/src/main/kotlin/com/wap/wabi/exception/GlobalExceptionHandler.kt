package com.wap.wabi.exception

import com.wap.wabi.common.payload.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    /*
     * Developer Custom Exception: 직접 정의한 RestApiException 에러 클래스에 대한 예외 처리
     */
    @ExceptionHandler(RestApiException::class)
    protected fun handleCustomException(ex: RestApiException): ResponseEntity<Response> {
        val errorCode = ex.errorCode
        return handleExceptionInternal(errorCode)
    }

    // handleExceptionInternal() 메소드를 오버라이딩해 응답 커스터마이징
    private fun handleExceptionInternal(errorCode: ErrorCode): ResponseEntity<Response> {
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(Response(errorCode.code, errorCode.message, data = null))
    }
}
