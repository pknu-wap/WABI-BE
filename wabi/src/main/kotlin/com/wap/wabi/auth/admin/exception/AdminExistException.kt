package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.WabiCodeException

class AdminExistException private constructor():WabiCodeException(AdminErrorCode.BAD_REQUEST_EXIST_ADMIN) {
    companion object{
        val EXCEPTION = AdminExistException()
    }
}