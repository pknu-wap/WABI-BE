package com.wap.wabi.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    @Value("\${jwt.expiration-hours}")
    private val expirationHours: Long,
    @Value("\${jwt.issuer}")
    private val issuer: String
) {
    fun createToken(userSpecification: String) = Jwts.builder()
        .signWith(
            SecretKeySpec(
                secretKey.toByteArray(),
                SignatureAlgorithm.HS512.jcaName
            )
        ) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
        .setSubject(userSpecification)   // JWT 토큰 제목
        .setIssuer(issuer)    // JWT 토큰 발급자
        .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
        .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰의 만료시간 설정
        .compact()!!    // JWT 토큰 생성

    fun validateTokenAndGetSubject(token: String): String? = Jwts.parserBuilder()
        .setSigningKey(secretKey.toByteArray())
        .build()
        .parseClaimsJws(token)
        .body
        .subject

    fun getAdminNameByToken(token: String): String {
        val subject =
            validateTokenAndGetSubject(token) ?: throw IllegalArgumentException("Invalid token")
        val (username) = subject.split(":")
        return username
    }
}
