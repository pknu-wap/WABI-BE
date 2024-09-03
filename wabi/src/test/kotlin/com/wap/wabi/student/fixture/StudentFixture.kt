package com.wap.wabi.student.fixture

import com.wap.wabi.student.entity.Student

object StudentFixture {
    fun createStudent(name: String): Student {
        return Student.builder()
            .id("201913050")
            .name(name)
            .build()
    }
}