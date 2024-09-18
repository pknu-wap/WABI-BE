package com.wap.wabi.auth.admin.service

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.auth.admin.payload.response.AdminLoginResponse
import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.auth.jwt.JwtTokenProvider
import com.wap.wabi.auth.admin.util.AdminRegisterValidator
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val tokenProvider: JwtTokenProvider,
    private val adminRegisterValidator: AdminRegisterValidator,
    private val encoder: PasswordEncoder
) {
    @Transactional
    fun registerAdmin(adminRegisterRequest: AdminRegisterRequest) {
        if (adminRepository.findAdminByName(adminRegisterRequest.name).isPresent) {
            throw RestApiException(ErrorCode.EXIST_ADMIN)
        }
        adminRegisterValidator.validate(adminRegisterRequest)
        adminRepository.save(adminRegisterRequest.toAdmin(encoder))
    }

    @Transactional
    fun loginAdmin(adminLoginRequest: AdminLoginRequest): AdminLoginResponse {
        val admin = adminRepository.findAdminByName(adminLoginRequest.name)
            ?.takeIf { admin ->
                encoder.matches(adminLoginRequest.password, admin.get().password)
            }
            ?: throw RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        val token = tokenProvider.createToken("${admin.get().username}:${admin.get().role}")
        return AdminLoginResponse(
            name = admin.get().username,
            role = admin.get().role,
            token = token)
    }
}
