package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.WabiCodeException

class AdminInvalidPwdException private constructor() : WabiCodeException(AdminErrorCode.BAD_REQUEST_ADMIN_PASSWORD) {
    companion object{
        val EXCEPTION = AdminInvalidPwdException()
    }
}