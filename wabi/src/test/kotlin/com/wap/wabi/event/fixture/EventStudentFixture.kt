package com.wap.wabi.event.fixture

import com.wap.wabi.band.entity.Band
import com.wap.wabi.common.Reflection
import com.wap.wabi.common.TestConstants
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.student.entity.Student

object EventStudentFixture {
    fun createEventStudent(event: Event, student: Student, band: Band): EventStudent {
        return EventStudent.builder()
            .event(event)
            .student(student)
            .band(band)
            .club(TestConstants.CLUB)
            .build()
    }

    fun createEventStudent(id: Long, event: Event, student: Student, band: Band): EventStudent {
        val eventStudent = createEventStudent(event, student, band)
        return Reflection.makeIdChangedClone(EventStudent::class.java, eventStudent, id)
    }
}