package com.wap.wabi.student.fixture

import com.wap.wabi.student.entity.Student

object StudentFixture {
    fun createStudent(name: String): Student {
        return Student.builder()
            .id("201912050")
            .name(name)
            .build()
    }

    fun createStudent(name: String, id: String): Student {
        return Student.builder()
            .id(id)
            .name(name)
            .build()
    }
}
