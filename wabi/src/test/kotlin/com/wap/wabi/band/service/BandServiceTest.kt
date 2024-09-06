package com.wap.wabi.band.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.fixture.BandStudentFixture
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.fixture.StudentFixture
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class BandServiceTest {
    @Autowired
    private lateinit var bandService: BandService

    @MockBean
    private lateinit var bandRepository: BandRepository

    @MockBean
    private lateinit var bandStudentRepository: BandStudentRepository

    @Test
    fun 밴드에_속한_학생들을_조회한다() {
        // Given
        val bandId = 1L
        val studentName1 = "Student 1"
        val studentName2 = "Student 2"
        val band = BandFixture.createBand("Band 1")
        val student1 = StudentFixture.createStudent(studentName1)
        val student2 = StudentFixture.createStudent(studentName2)
        val bandStudents = listOf(
            BandStudentFixture.createBandStudent(student1, band),
            BandStudentFixture.createBandStudent(student2, band)
        )

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(band))
        `when`(bandStudentRepository.findAllByBand(band)).thenReturn(bandStudents)

        // When
        val result = bandService.getBandStudents(bandId)

        // Then
        assertAll(
            { assertThat(result.size).isEqualTo(2) },
            { assertThat(result[0].name).isEqualTo(studentName1) },
            { assertThat(result[1].name).isEqualTo(studentName2) }
        )
    }

    @Test
    fun 유효하지_않은_밴드Id_값을_입력하면_NOT_FOUND_BAND_예외를_반환한다() {
        // Given
        val invalidBandId = 2L

        `when`(bandRepository.findById(invalidBandId)).thenReturn(Optional.empty())

        // When & Then
        val exception = assertThrows<RestApiException> {
            bandService.getBandStudents(invalidBandId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_FOUND_BAND)
    }
}
