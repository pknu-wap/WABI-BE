package com.wap.wabi.admin.payload.request

import com.wap.wabi.admin.entity.Admin
import com.wap.wabi.admin.entity.Enum.AdminStatus

data class AdminRegisterRequest(
    val id: String,
    val password: String,
    val name: String
) {
    fun toAdmin(): Admin {
        return Admin.builder()
            .id(this.id)
            .password(this.password)
            .name(this.name)
            .status(AdminStatus.ACTIVE)
            .build()
    }
}
