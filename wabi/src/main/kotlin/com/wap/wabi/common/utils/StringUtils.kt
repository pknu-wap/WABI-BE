package com.wap.wabi.common.utils

import java.util.regex.Pattern

class StringUtils {
    companion object {
        @JvmStatic
        fun checkLength(string: String, start: Int, end: Int): Boolean {
            val length = string.length
            return length in start..end
        }

        @JvmStatic
        fun hasOnlySmallLetterOrNumber(string: String): Boolean {
            val pattern = "^[a-z0-9]*$".toRegex()
            return pattern.matches(string)
        }

        @JvmStatic
        fun hasOnlyAllowedSpecialCharacters(input: String, specialCharacters: String): Boolean {
            val allowedPattern = "^[a-zA-Z0-9${Pattern.quote(specialCharacters)}]*$".toRegex()
            val containsSpecialChar = input.any { it in specialCharacters }

            return allowedPattern.matches(input) && containsSpecialChar
        }
    }
}
