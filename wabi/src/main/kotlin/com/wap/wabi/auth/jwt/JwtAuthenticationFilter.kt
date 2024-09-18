package com.wap.wabi.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = parseBearerToken(request)

        if (token != null && jwtTokenProvider.validateTokenAndGetSubject(token) != null) {
            val user = parseUserSpecification(token)
            UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                .apply { details = WebAuthenticationDetails(request) }
                .also { SecurityContextHolder.getContext().authentication = it }
        } else {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    private fun parseBearerToken(request: HttpServletRequest): String? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
            ?.substring(7)
    }

    private fun parseUserSpecification(token: String): User {
        val subject =
            jwtTokenProvider.validateTokenAndGetSubject(token) ?: throw IllegalArgumentException("Invalid token")
        val (username, role) = subject.split(":")
        return User(username, "", listOf(SimpleGrantedAuthority(role)))
    }
}
