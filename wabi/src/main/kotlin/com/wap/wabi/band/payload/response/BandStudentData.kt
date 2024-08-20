package com.wap.wabi.band.payload.response

import com.wap.wabi.band.entity.BandStudent
import java.time.LocalDate
import java.time.LocalDateTime

data class BandStudentData(
    val studentId: String,
    val name: String,
    val club: String,
    val position: String,
    val joinDate: LocalDate,
    val college: String,
    val major: String,
    val tel: String,
    val academicStatus: String
){
    companion object{
        fun of(bandStudent : List<BandStudent>) : List<BandStudentData>{
            return bandStudent.map { eventStudent ->
                of(eventStudent)
            }
        }
        fun of(bandStudent : BandStudent) : BandStudentData {
            return BandStudentData(
                studentId = bandStudent.student.id,
                name = bandStudent.student.name,
                club = bandStudent.club,
                position = bandStudent.position,
                joinDate = bandStudent.joinDate,
                college = bandStudent.college,
                major = bandStudent.college,
                tel = bandStudent.tel,
                academicStatus = bandStudent.academicStatus
            )
        }
    }
}

