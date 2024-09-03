package com.wap.wabi.band.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class BandServiceTest {

    @MockBean
    private lateinit var bandRepository: BandRepository

    @MockBean
    private lateinit var bandStudentRepository: BandStudentRepository

    @Test
    fun 밴드에_속한_학생들을_조회한다() {
        // Given
        val bandId = 1L
        val band = Band(bandId, "Test Band")
        val bandStudents = listOf(
            BandStudent(id = 1L, name = "Student 1", band = band),
            BandStudent(id = 2L, name = "Student 2", band = band)
        )

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(band))
        `when`(bandStudentRepository.findAllByBand(band)).thenReturn(bandStudents)

        // When
        val result = bandService.getBandStudents(bandId)

        // Then
        assertEquals(2, result.size)
        assertEquals("Student 1", result[0].name)
        assertEquals("Student 2", result[1].name)
    }

    @Test
    fun `getBandStudents should throw RestApiException when band not found`() {
        // Given
        val bandId = 1L

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.empty())

        // When / Then
        val exception = assertThrows<RestApiException> {
            bandService.getBandStudents(bandId)
        }

        assertEquals(ErrorCode.NOT_FOUND_BAND, exception.errorCode)
    }
}
