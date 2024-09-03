package com.wap.wabi.band.fixture

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.student.entity.Student
import java.time.LocalDate

object BandStudentFixture {

    fun createBandStudent(student: Student, band: Band): BandStudent {
        return BandStudent.builder()
            .student(student)
            .band(band)
            .tel("010")
            .club("WAP")
            .major("컴퓨터공학전공")
            .college("정보융합대학")
            .academicStatus("재학")
            .joinDate(LocalDate.parse("2022-09-03"))
            .position("회장")
            .build()
    }
}