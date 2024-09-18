package com.wap.wabi.auth.admin.service

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.auth.admin.payload.response.AdminLoginResponse
import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val tokenProvider: JwtTokenProvider
) {
    @Transactional
    fun registerAdmin(adminRegisterRequest: AdminRegisterRequest){
        if (adminRepository.findAdminByName(adminRegisterRequest.name).isPresent){
            throw RestApiException(ErrorCode.EXIST_ADMIN)
        }
        adminRepository.save(adminRegisterRequest.toAdmin())
    }

    @Transactional
    fun loginAdmin(adminLoginRequest: AdminLoginRequest): AdminLoginResponse {
        val admin = adminRepository.findAdminByName(adminLoginRequest.name)
            ?: throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        val token = tokenProvider.createToken("${admin.get().username}")
        return AdminLoginResponse(admin.get().username, token)
    }
}