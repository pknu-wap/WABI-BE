package com.wap.wabi.band.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.band.payload.request.EnrollRequest
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.student.fixture.StudentFixture
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.util.*

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class BandEnrollServiceTest {
    @Autowired
    private lateinit var bandEnrollService: BandEnrollService

    @MockBean
    private lateinit var bandRepository: BandRepository

    @MockBean
    private lateinit var studentRepository: StudentRepository

    @Test
    fun 밴드에_학생_정보를_저장한다() {
        // Given
        val bandId = 1L
        val band = BandFixture.createBand("Band 1")

        val studentId = "201913050"
        val name = "김종경"
        val club = "WAP"
        val position = "회원"
        val joinDate = LocalDate.parse("2023-09-03")
        val college = "정보융합대학"
        val major = "컴퓨터공학전공"
        val tel = "010-6406-9778"
        val academicStatus = "재학"
        val bandStudentDto = BandStudentDto(
            studentId = studentId,
            name = name,
            club = club,
            position = position,
            joinDate = joinDate,
            college = college,
            major = major,
            tel = tel,
            academicStatus = academicStatus
        )
        val bandStudentDtos: MutableList<BandStudentDto> = mutableListOf()
        bandStudentDtos.add(bandStudentDto)
        val enrollRequest = EnrollRequest(
            bandStudentDtos
        )

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(band))
        `when`(studentRepository.findById(studentId)).thenReturn(Optional.of(StudentFixture.createStudent("김종경")))

        //When & Then
        Assertions.assertDoesNotThrow {
            bandEnrollService.enrollBandStudent(bandId = bandId, request = enrollRequest)
        }
    }
}