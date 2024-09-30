package com.wap.wabi.event.fixture

import com.wap.wabi.common.Reflection
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.student.entity.Student

object EventStudentFixture {
    fun createEventStudent(event: Event, student: Student): EventStudent {
        return EventStudent.builder()
            .event(event)
            .student(student)
            .build()
    }

    fun createEventStudent(id: Long, event: Event, student: Student): EventStudent {
        val eventStudent = createEventStudent(event, student)
        return Reflection.makeIdChangedClone(EventStudent::class.java, eventStudent, id)
    }
}
