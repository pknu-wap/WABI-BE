package com.wap.wabi.auth.admin.payload.request

import com.wap.wabi.auth.admin.entity.Admin
import com.wap.wabi.auth.admin.entity.Enum.AdminRole
import com.wap.wabi.auth.admin.entity.Enum.AdminStatus
import com.wap.wabi.common.utils.StringUtils.Companion.checkLength
import com.wap.wabi.common.utils.StringUtils.Companion.hasOnlyAllowedSpecialCharacters
import com.wap.wabi.common.utils.StringUtils.Companion.hasOnlySmallLetterOrNumber
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.security.crypto.password.PasswordEncoder

data class AdminRegisterRequest(
    val name: String,
    val password: String,
    val email: String
) {
    fun toAdmin(encoder: PasswordEncoder): Admin {
        validate()
        return Admin.builder()
            .name(this.name)
            .password(encoder.encode(this.password))
            .email(this.email)
            .role(AdminRole.USER)
            .status(AdminStatus.ACTIVE)
            .build()
    }
    private fun validate() {
        if (!isCorrectName() || !isCorrectPassword()) {
            throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        }
    }

    private fun isCorrectName(): Boolean {
        return name.isNotBlank() &&
                checkLength(name, 4, 10) &&
                hasOnlySmallLetterOrNumber(name)
    }

    private fun isCorrectPassword(): Boolean {
        return password.isNotBlank() &&
                checkLength(password, 8, 15) &&
                hasOnlyAllowedSpecialCharacters(password, "~!@#")
    }
}
