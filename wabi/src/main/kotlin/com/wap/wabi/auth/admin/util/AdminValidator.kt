package com.wap.wabi.auth.admin.util

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.common.utils.StringUtils
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.stereotype.Component

@Component
class AdminValidator {
    fun validateRegister(adminRegisterRequest: AdminRegisterRequest) {
        validateCommon(adminRegisterRequest.name, adminRegisterRequest.password)
    }

    fun validateLogin(adminLoginRequest: AdminLoginRequest) {
        validateCommon(adminLoginRequest.name, adminLoginRequest.password)
    }

    private fun validateCommon(name: String, password: String) {
        if (!isCorrectName(name)) {
            throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN_NAME)
        }
        if (!isCorrectPassword(password)) {
            throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
        }
    }

    private fun isCorrectName(name: String): Boolean {
        return name.isNotBlank() &&
                StringUtils.checkLength(name, 4, 10) &&
                StringUtils.hasOnlySmallLetterOrNumber(name)
    }

    private fun isCorrectPassword(password: String): Boolean {
        return password.isNotBlank() &&
                StringUtils.checkLength(password, 8, 15) &&
                StringUtils.hasOnlyAllowedSpecialCharacters(password, "~!@#")
    }
}
