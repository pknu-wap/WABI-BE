package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.WabiCodeException

class AdminExpireTokenException private constructor() : WabiCodeException(AdminErrorCode.UNAUTHORIZED_ADMIN_EXPIRE_TOKEN) {
    companion object{
        val EXCEPTION = AdminExpireTokenException()
    }
}