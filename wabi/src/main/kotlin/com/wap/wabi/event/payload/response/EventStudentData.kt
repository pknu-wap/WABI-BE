package com.wap.wabi.event.payload.response

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.event.entity.EventStudentBandName
import java.time.LocalDateTime

data class EventStudentData(
    val id: String,
    val name: String,
    val eventStudentStatus: EventStudentStatus,
    val checkInTime: LocalDateTime?,
    val bandName: String
) {
    companion object {
        fun of(eventStudent: EventStudent, bandNames: List<EventStudentBandName>): EventStudentData {
            return EventStudentData(
                eventStudent.student.id,
                eventStudent.student.name,
                eventStudent.status,
                eventStudent.checkedInAt,
                bandNames.joinToString(", ") { it.bandName }
            )
        }
    }
}
