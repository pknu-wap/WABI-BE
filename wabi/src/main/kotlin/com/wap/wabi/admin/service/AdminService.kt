package com.wap.wabi.admin.service

import com.wap.wabi.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.admin.repository.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {
    @Transactional
    fun registerAdmin(adminRegisterRequest: AdminRegisterRequest){
        adminRepository.save(adminRegisterRequest.toAdmin())
    }
}