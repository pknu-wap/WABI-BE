package com.wap.wabi.admin.service

import com.wap.wabi.auth.admin.payload.request.AdminLoginRequest
import com.wap.wabi.auth.admin.payload.request.AdminRegisterRequest
import com.wap.wabi.auth.admin.service.AdminService
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.junit.jupiter.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class AdminServiceTest {
    @Autowired
    private lateinit var adminService: AdminService

    companion object {
        const val CORRECT_NAME = "abcd11"
        const val CORRECT_PASSWORD = "12345678@"
        const val TEST_EMAIL = "abc@naver.com"
    }

    @Nested
    inner class AdminRegisterTest {
        @Test
        fun `관리자 등록 시엔 정해진 조건에 맞는 name, pwd, email 이 필요하다`() {
            //Given
            val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, CORRECT_PASSWORD, TEST_EMAIL)
            //When & Then
            Assertions.assertDoesNotThrow {
                adminService.registerAdmin(adminRegisterRequest)
            }

        }

        @Nested
        inner class AdminRegisterIdTest {
            @ParameterizedTest
            @ValueSource(strings = ["", "!", "A", "asdfㅁ"])
            fun `이름에 알파벳 소문자 및 숫자가 아닌 문자가 하나라도 들어가있다면 실패한다`(input: String) {
                //Given
                val adminRegisterRequest = AdminRegisterRequest(input, CORRECT_PASSWORD, TEST_EMAIL)

                //When
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                //Then
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_NAME)
            }

            @ParameterizedTest
            @ValueSource(strings = ["asdf", "abcddd12", "123456789a"])
            fun `이름은 최소 4자 이상, 10자 이하여야만 한다`(input: String) {
                //Given
                val adminRegisterRequest = AdminRegisterRequest(input, CORRECT_PASSWORD, TEST_EMAIL)

                //When & Then
                Assertions.assertDoesNotThrow {
                    adminService.registerAdmin(adminRegisterRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["asd", "123456789ab"])
            fun `이름은 최소 4자 이상, 10자 이하가 아니라면 실패한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(input, CORRECT_PASSWORD, TEST_EMAIL)
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                //Then
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_NAME)
            }

            @Test
            fun `이미 있는 이름은 가입할 수 없다`() {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, CORRECT_PASSWORD, TEST_EMAIL)
                adminService.registerAdmin(adminRegisterRequest)
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_EXIST_ADMIN)
            }
        }

        @Nested
        inner class AdminRegisterPwdTest {
            @ParameterizedTest
            @ValueSource(strings = ["1234567A~", "12345abcdA!"])
            fun `비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 하나로 구성한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)

                Assertions.assertDoesNotThrow {
                    adminService.registerAdmin(adminRegisterRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["", "123456@", "123456789abcdfd@"])
            fun `비밀번호가 최소 8자 이상, 15자 이하가 아니라면 실패한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                //Then
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }

            @ParameterizedTest
            @ValueSource(strings = ["1234567A,", "1234567가,"])
            fun `비밀번호에 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 아닌 문자가 있다면 실패한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                //Then
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }

            @ParameterizedTest
            @ValueSource(strings = ["1234567A", "123456789abcd"])
            fun `비밀번호는 특수문자(~!@#)가 반드시 하나 이상 있어야 한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)
                val exception = assertThrows<RestApiException> {
                    adminService.registerAdmin(adminRegisterRequest)
                }
                //Then
                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }
        }
    }

    @Nested
    inner class AdminLoginTest {
        @Nested
        inner class AdminLoginNameTest {

            @Test
            fun `이름은 알파벳 소문자 및 숫자로만 구성되어야 한다`() {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, CORRECT_PASSWORD, TEST_EMAIL)
                adminService.registerAdmin(adminRegisterRequest)

                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, CORRECT_PASSWORD)

                Assertions.assertDoesNotThrow {
                    adminService.loginAdmin(adminLoginRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["abcd", "abcde", "123456789a"])
            fun `이름은 최소 4자 이상, 10자 이하여야만 한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(input, CORRECT_PASSWORD, TEST_EMAIL)
                adminService.registerAdmin(adminRegisterRequest)

                val adminLoginRequest = AdminLoginRequest(input, CORRECT_PASSWORD)

                Assertions.assertDoesNotThrow {
                    adminService.loginAdmin(adminLoginRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["1234567A", "1234567@"])
            fun `이름에 알파벳 소문자 및 숫자가 아닌 문자가 하나라도 들어가있다면 실패한다`(input: String) {
                val adminLoginRequest = AdminLoginRequest(input, CORRECT_PASSWORD)

                val exception = assertThrows<RestApiException> {
                    adminService.loginAdmin(adminLoginRequest)
                }

                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_NAME)
            }

            @ParameterizedTest
            @ValueSource(strings = ["", "abc", "123456789ab"])
            fun `이름은 최소 4자 이상, 10자 이하가 아니라면 실패한다`(input: String) {
                val adminLoginRequest = AdminLoginRequest(input, CORRECT_PASSWORD)

                val exception = assertThrows<RestApiException> {
                    adminService.loginAdmin(adminLoginRequest)
                }

                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_NAME)
            }
        }

        @Nested
        inner class AdminLoginPwdTest {
            @ParameterizedTest
            @ValueSource(strings = ["1234567@", "123456789abcdf@"])
            fun `비밀번호는 최소 8자 이상, 15자 이하 이어야만 한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)
                adminService.registerAdmin(adminRegisterRequest)

                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, input)

                Assertions.assertDoesNotThrow {
                    adminService.loginAdmin(adminLoginRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["123456@", "123456789abcdfd@", ""])
            fun `비밀번호가 최소 8자 이상, 15자 이하가 아니라면 실패한다`(input: String) {
                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, input)

                val exception = assertThrows<RestApiException> {
                    adminService.loginAdmin(adminLoginRequest)
                }

                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }

            @ParameterizedTest
            @ValueSource(strings = ["123456789Za@", "123456789!@#"])
            fun `비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 하나로 구성한다`(input: String) {
                val adminRegisterRequest = AdminRegisterRequest(CORRECT_NAME, input, TEST_EMAIL)
                adminService.registerAdmin(adminRegisterRequest)

                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, input)

                Assertions.assertDoesNotThrow {
                    adminService.loginAdmin(adminLoginRequest)
                }
            }

            @ParameterizedTest
            @ValueSource(strings = ["123456789Za`", "123456789@\\ "])
            fun `비밀번호에 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(~!@#)중 아닌 문자가 있다면 실패한다`(input: String) {
                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, input)

                val exception = assertThrows<RestApiException> {
                    adminService.loginAdmin(adminLoginRequest)
                }

                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }

            @ParameterizedTest
            @ValueSource(strings = ["123456789Za", "123456789"])
            fun `비밀번호에 특수문자(~!@#)가 하나라도 없으면 실패한다`(input: String) {
                val adminLoginRequest = AdminLoginRequest(CORRECT_NAME, input)

                val exception = assertThrows<RestApiException> {
                    adminService.loginAdmin(adminLoginRequest)
                }

                assertThat(exception.errorCode).isEqualTo(ErrorCode.BAD_REQUEST_ADMIN_PASSWORD)
            }

        }
    }
}
