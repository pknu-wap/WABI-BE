package com.wap.wabi.band.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.fixture.BandStudentFixture
import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.fixture.StudentFixture
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers
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

        // When
        val exception = assertThrows<RestApiException> {
            bandService.getBandStudents(invalidBandId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_FOUND_BAND)
    }

    @Test
    fun 밴드를_생성한다() {
        //Given
        val adminId = 1L
        val bandName = "band 1"
        val bandCreateRequest = BandCreateRequest(
            bandName = bandName,
        )

        val savedBand = BandFixture.createBand(id = 1, name = bandName)

        `when`(bandRepository.save(ArgumentMatchers.any())).thenReturn(savedBand)

        //When & Then
        Assertions.assertDoesNotThrow {
            bandService.createBand(adminId = adminId, bandCreateRequest = bandCreateRequest)
        }
    }

    @Test
    fun 밴드_생성_시_유효하지_않은_adminId_값을_입력하면_UNAUTHORIZED_REQUEST_예외를_반환한다() {
        // Given
        val invalidAdminId = 2L
        val bandName = "band 1"
        val bandCreateRequest = BandCreateRequest(
            bandName = bandName,
        )

        // When
        val exception = assertThrows<RestApiException> {
            bandService.createBand(adminId = invalidAdminId, bandCreateRequest = bandCreateRequest)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_REQUEST)
    }

    @Test
    fun 밴드를_삭제한다() {
        //Given
        val adminId = 1L
        val bandId = 1L
        val bandName = "band 1"

        val savedBand = BandFixture.createBand(id = 1, name = bandName)

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

        //When & Then
        Assertions.assertDoesNotThrow {
            bandService.deleteBand(adminId = adminId, bandId = bandId)
        }
    }

    @Test
    fun 밴드_삭제_시_유효하지_않은_bandId_값을_입력하면_NOT_FOUND_BAND_예외를_반환한다() {
        // Given
        val adminId = 1L
        val invalidBandId = 2L

        `when`(bandRepository.findById(invalidBandId)).thenReturn(Optional.empty())

        // When
        val exception = assertThrows<RestApiException> {
            bandService.deleteBand(adminId = adminId, bandId = adminId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_FOUND_BAND)
    }

    @Test
    fun 밴드_삭제_시_유효하지_않은_adminId_값을_입력하면_UNAUTHORIZED_REQUEST_예외를_반환한다() {
        // Given
        val invalidAdminId = 2L
        val bandId = 1L

        // When
        val exception = assertThrows<RestApiException> {
            bandService.deleteBand(adminId = invalidAdminId, bandId = bandId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_REQUEST)
    }

    @Test
    fun 밴드_삭제_시_학생명단이_추가된_밴드를_삭제_하려고_하면_ALREADY_ADD_STUDENT_예외를_반환한다() {
        // Given
        val adminId = 1L
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
        val exception = assertThrows<RestApiException> {
            bandService.deleteBand(adminId = adminId, bandId = bandId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.ALREADY_ADD_STUDENT)
    }
}
