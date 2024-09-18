package com.wap.wabi.auth.jwt.service

import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AdminDetailService(
    private val adminRepository: AdminRepository
) : UserDetailsService{
    override fun loadUserByUsername(username: String?): UserDetails {
        return adminRepository.findAdminByName(username).orElseThrow {
            RestApiException(ErrorCode.BAD_REQUEST_ADMIN)
        }
    }
}