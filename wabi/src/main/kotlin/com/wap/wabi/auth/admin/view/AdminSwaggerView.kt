package com.wap.wabi.auth.admin.view

object AdminSwaggerView {
    const val ADMIN_REGISTER_SUMMARY = "관리자 회원가입"
    const val ADMIN_LOGIN_SUMMARY = "관리자 로그인"
    const val ADMIN_NAME_AND_PWD_ROLE_DESCRIPTION = "**이름 조건**\n" +
            "\n" +
            "1. 최소 4자 이상, 10자 이하\n" +
            "2. 알파벳 소문자(a~z), 숫자(0~9) 로만 구성\n" +
            "\n" +
            "**비밀번호 조건**\n" +
            "\n" +
            "1. 최소 8자 이상, 15자 이하\n" +
            "2. 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(**~!@#)** 중 하나로 구성\n" +
            "3. 특수문자는 반드시 하나 이상 있어야 한다"
}
