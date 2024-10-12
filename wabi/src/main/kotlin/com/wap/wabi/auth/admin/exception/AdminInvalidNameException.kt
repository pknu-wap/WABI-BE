package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.WabiCodeException

class AdminInvalidNameException private constructor() : WabiCodeException(AdminErrorCode.BAD_REQUEST_ADMIN_NAME) {
    companion object {
        val EXCEPTION = AdminInvalidNameException()
    }
}
