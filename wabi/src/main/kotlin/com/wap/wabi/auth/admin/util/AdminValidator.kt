package com.wap.wabi.auth.admin.util

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.common.utils.StringUtils
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.stereotype.Component

@Component
class AdminValidator {
    fun validate(adminRegisterRequest: AdminRegisterRequest) {
        if (!isCorrectName(adminRegisterRequest.name) || !isCorrectPassword(adminRegisterRequest.password)) {
            throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        }
    }

    fun validate(adminLoginRequest: AdminLoginRequest){
        if (!isCorrectName(adminLoginRequest.name) || !isCorrectPassword(adminLoginRequest.password)) {
            throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        }
    }

    private fun isCorrectName(name: String): Boolean {
        return name.isNotBlank() &&
                StringUtils.checkLength(name, 4, 10) &&
                StringUtils.hasOnlySmallLetterOrNumber(name)
    }

    private fun isCorrectPassword(password : String): Boolean {
        return password.isNotBlank() &&
                StringUtils.checkLength(password, 8, 15) &&
                StringUtils.hasOnlyAllowedSpecialCharacters(password, "~!@#")
    }
}
