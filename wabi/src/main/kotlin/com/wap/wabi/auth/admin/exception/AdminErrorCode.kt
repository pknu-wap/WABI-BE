package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.BaseErrorCode
import com.wap.wabi.exception.payload.response.ErrorReason
import org.springframework.http.HttpStatus

enum class AdminErrorCode(
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val code: String = "",
    val message: String = ""
) : BaseErrorCode {

    BAD_REQUEST_ADMIN_PASSWORD(HttpStatus.BAD_REQUEST,"Admin_400_1","비밀번호가 형식에 맞지 않습니다."),
    BAD_REQUEST_ADMIN_NAME(HttpStatus.BAD_REQUEST,"Admin_400_2","이름이 형식에 맞지 않습니다."),
    BAD_REQUEST_EXIST_ADMIN(HttpStatus.BAD_REQUEST, "Admin_400_3","이미 존재하는 어드민입니다."),
    BAD_REQUEST_NOT_EXIST_ADMIN(HttpStatus.BAD_REQUEST, "Admin_400_4","이미 존재하는 어드민입니다."),
    UNAUTHORIZED_ADMIN_EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED,"Admin_401_1","만료된 어드민 정보입니다.");

    override fun getErrorReason(): ErrorReason {
        return ErrorReason(httpStatus, code, message)
    }
}
