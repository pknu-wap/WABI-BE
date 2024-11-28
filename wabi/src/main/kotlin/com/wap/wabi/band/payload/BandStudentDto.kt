package com.wap.wabi.band.payload

import java.time.LocalDate

data class BandStudentDto(
    val studentId: String,
    val name: String,
    val club: String?,
    val position: String?,
    val joinDate: LocalDate?,
    val college: String?,
    val major: String?,
    val tel: String?,
    val academicStatus: String?
)
