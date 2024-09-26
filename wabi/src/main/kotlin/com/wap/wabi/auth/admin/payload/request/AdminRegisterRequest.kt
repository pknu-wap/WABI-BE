package com.wap.wabi.auth.admin.payload.request

import com.wap.wabi.auth.admin.entity.Admin
import com.wap.wabi.auth.admin.entity.Enum.AdminRole
import com.wap.wabi.auth.admin.entity.Enum.AdminStatus
import org.springframework.security.crypto.password.PasswordEncoder

data class AdminRegisterRequest(
    val name: String,
    val password: String,
    val email: String
) {
    fun toAdmin(encoder: PasswordEncoder): Admin {
        return Admin.builder()
            .name(this.name)
            .password(encoder.encode(this.password))
            .email(this.email)
            .role(AdminRole.USER)
            .status(AdminStatus.ACTIVE)
            .build()
    }
}
