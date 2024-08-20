package com.wap.wabi.event.payload.response

import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.entity.EventStudent
import java.time.LocalDateTime

data class EventStudentData(
    val id : String,
    val name : String,
    val group : String,
    val eventStudentStatus : EventStudentStatus,
    val checkInTime : LocalDateTime
){
    companion object{
        fun of(eventStudents : List<EventStudent>) : List<EventStudentData>{
            return eventStudents.map { eventStudent ->
                of(eventStudent)
            }
        }
        fun of(eventStudent : EventStudent) : EventStudentData{
            return EventStudentData(
                eventStudent.student.id,
                eventStudent.student.name,
                eventStudent.studentGroup,
                eventStudent.status,
                eventStudent.checkedInAt
            )
        }
    }
}
