package com.wap.wabi.auth.admin.service

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.auth.admin.payload.response.AdminLoginResponse
import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.auth.admin.util.AdminValidator
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val tokenProvider: JwtTokenProvider,
    private val adminValidator: AdminValidator,
    private val encoder: PasswordEncoder
) {
    @Transactional
    fun registerAdmin(adminRegisterRequest: AdminRegisterRequest) {
        adminValidator.validateRegister(adminRegisterRequest)
        if (adminRepository.findByName(adminRegisterRequest.name).isPresent) {
            throw RestApiException(ErrorCode.BAD_REQUEST_EXIST_ADMIN)
        }
        adminRepository.save(adminRegisterRequest.toAdmin(encoder))
    }

    @Transactional
    fun loginAdmin(adminLoginRequest: AdminLoginRequest): AdminLoginResponse {
        adminValidator.validateLogin(adminLoginRequest)
        val admin = adminRepository.findByName(adminLoginRequest.name)
            ?.takeIf { admin ->
                encoder.matches(adminLoginRequest.password, admin.get().password)
            }
            ?: throw RestApiException(ErrorCode.BAD_REQUEST_NOT_EXIST_ADMIN)
        val token = tokenProvider.createToken("${admin.get().username}:${admin.get().role}")
        return AdminLoginResponse(
            name = admin.get().username,
            role = admin.get().role,
            token = token
        )
    }

    fun getAdminId(adminName: String): Long {
        val admin = adminRepository.findByName(adminName)
            ?: throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        return admin.get().id
    }
}
