package com.wap.wabi.auth.admin.service

import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {
    @Transactional
    fun registerAdmin(adminRegisterRequest: AdminRegisterRequest){
        if (adminRepository.findAdminByIdAndPassword(adminRegisterRequest.id).isPresent){
            throw RestApiException(ErrorCode.EXIST_ADMIN)
        }
        adminRepository.save(adminRegisterRequest.toAdmin())
    }
}