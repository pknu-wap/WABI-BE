package com.wap.wabi.event.payload.response

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.EventStudent
import java.time.LocalDateTime

data class EventStudentData(
    val id: String,
    val name: String,
    val band: String,
    val eventStudentStatus: EventStudentStatus,
    val checkInTime: LocalDateTime?
) {
    companion object {
        fun of(eventStudents: List<EventStudent>): List<EventStudentData> {
            return eventStudents.map {
                of(it)
            }
        }

        fun of(eventStudent: EventStudent): EventStudentData {
            return EventStudentData(
                eventStudent.student.id,
                eventStudent.student.name,
                eventStudent.band.bandName,
                eventStudent.status,
                eventStudent.checkedInAt
            )
        }
    }
}
