package com.wap.wabi.auth.admin.exception

import com.wap.wabi.exception.WabiCodeException

class AdminNotExistException private constructor() : WabiCodeException(AdminErrorCode.BAD_REQUEST_NOT_EXIST_ADMIN){
    companion object{
        val EXCEPTION = AdminNotExistException()
    }
}