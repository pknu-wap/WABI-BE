package com.wap.wabi.admin.service

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class AdminServiceTest {
    @Autowired
    private lateinit var adminService: AdminService

    @Nested
    inner class AdminRegisterTest {
        @Test
        fun `관리자 등록 시엔 정해진 조건에 맞는 id, pwd, bandName 이 필요하다`() {

        }

        @Nested
        inner class AdminRegisterIdTest {
            @Test
            fun `아이디는 알파벳 소문자 및 숫자로만 구성되어야 한다`() {

            }

            @Test
            fun `아이디에 알파벳 소문자 및 숫자가 아닌 문자가 하나라도 들어가있다면 실패한다`() {

            }

            @Test
            fun `아이디는 최소 4자 이상, 10자 이하여야만 한다`() {

            }

            @Test
            fun `아이디는 최소 4자 이상, 10자 이하가 아니라면 실패한다`() {

            }
        }

        @Nested
        inner class AdminRegisterPwdTest {
            @Test
            fun `비밀번호는 최소 8자 이상, 15자 이하 이어야만 한다`() {

            }

            @Test
            fun `비밀번호가 최소 8자 이상, 15자 이하가 아니라면 실패한다`() {

            }

            @Test
            fun `비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 하나로 구성한다`() {

            }

            @Test
            fun `비밀번호에 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 아닌 문자가 있다면 실패한다`() {

            }

            @Test
            fun `비밀번호는 특수문자(~!@#)가 반드시 하나 이상 있어야 한다`() {

            }

            @Test
            fun `비밀번호에 특수문자(~!@#)가 없으면 실패한다`() {

            }
        }
    }

    @Nested
    inner class AdminLoginTest {
        @Test
        fun `올바른 유저 아이디 및 비밀번호일 경우 로그인이 가능하다`() {

        }

        @Nested
        inner class AdminLoginIdTest {
            @Test
            fun `아이디는 알파벳 소문자 및 숫자로만 구성되어야 한다`() {

            }

            @Test
            fun `아이디에 알파벳 소문자 및 숫자가 아닌 문자가 하나라도 들어가있다면 실패한다`() {

            }

            @Test
            fun `아이디는 최소 4자 이상, 10자 이하여야만 한다`() {

            }

            @Test
            fun `아이디는 최소 4자 이상, 10자 이하가 아니라면 실패한다`() {

            }
        }

        @Nested
        inner class AdminLoginPwdTest {
            @Test
            fun `비밀번호는 최소 8자 이상, 15자 이하 이어야만 한다`() {

            }

            @Test
            fun `비밀번호가 최소 8자 이상, 15자 이하가 아니라면 실패한다`() {

            }

            @Test
            fun `비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 하나로 구성한다`() {

            }

            @Test
            fun `비밀번호에 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 아닌 문자가 있다면 실패한다`() {

            }

            @Test
            fun `비밀번호는 특수문자(~!@#)가 반드시 하나 이상 있어야 한다`() {

            }

            @Test
            fun `비밀번호에 특수문자(~!@#)가 없으면 실패한다`() {

            }
        }
    }
}