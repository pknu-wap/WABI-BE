package com.wap.wabi.auth.admin.payload.request

import com.wap.wabi.auth.admin.entity.Admin
import com.wap.wabi.auth.admin.entity.Enum.AdminStatus

data class AdminRegisterRequest(
    val name: String,
    val password: String,
    val email: String
) {
    fun toAdmin(): Admin {
        return Admin.builder()
            .name(this.name)
            .password(this.password)
            .email(this.email)
            .status(AdminStatus.ACTIVE)
            .build()
    }
}
