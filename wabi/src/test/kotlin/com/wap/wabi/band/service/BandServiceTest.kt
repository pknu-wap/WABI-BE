package com.wap.wabi.band.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.fixture.BandStudentFixture
import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.payload.request.BandUpdateRequest
import com.wap.wabi.band.payload.response.BandsData
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
import java.util.Optional

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
            bandMemo = "band memo"
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
            bandMemo = "band memo"
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
    fun 밴드_삭제_시_자신이_생성한_밴드가_아니면_UNAUTHORIZED_BAND_예외를_반환한다() {
        // Given
        val adminId = 1L
        val bandId = 1L
        val savedBand = BandFixture.createAnotherUserBand(id = 1, name = "bandName")

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

        // When
        val exception = assertThrows<RestApiException> {
            bandService.deleteBand(adminId = adminId, bandId = bandId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_BAND)
    }

    @Test
    fun 밴드_삭제_시_유효하지_않은_adminId_값을_입력하면_UNAUTHORIZED_REQUEST_예외를_반환한다() {
        // Given
        val invalidAdminId = 2L
        val bandId = 1L
        val bandName = "band 1"

        val savedBand = BandFixture.createBand(id = 1, name = bandName)

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

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

    @Test
    fun 내가_관리하는_밴드_목록을_조회한다() {
        // Given
        val adminId = 1L
        val band1Id = 1L
        val band2Id = 2L
        val band1 = BandFixture.createBand("Band 1", 1)
        val band2 = BandFixture.createBand("Band 2", 2)

        val bandsResponse: MutableList<BandsData> = mutableListOf()
        bandsResponse.add(BandsData(bandId = band1Id, bandName = "Band 1"))
        bandsResponse.add(BandsData(bandId = band2Id, bandName = "Band 2"))

        `when`(bandRepository.findAllByAdminId(adminId)).thenReturn(listOf(band1, band2))

        // When
        val result = bandService.getBands(adminId = adminId)

        // Then
        assertAll(
            { assertThat(result[0].bandId).isEqualTo(band1Id) },
            { assertThat(result[1].bandId).isEqualTo(band2Id) },
            { assertThat(result[0].bandName).isEqualTo("Band 1") },
            { assertThat(result[1].bandName).isEqualTo("Band 2") }
        )
    }

    @Test
    fun 밴드_목록_조회_시_유효하지_않은_adminId_값을_입력하면_UNAUTHORIZED_REQUEST_예외를_반환한다() {
        // Given
        val invalidAdminId = 2L

        // When
        val exception = assertThrows<RestApiException> {
            bandService.getBands(adminId = invalidAdminId)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_REQUEST)
    }

    @Test
    fun 밴드를_수정한다() {
        // Given
        val adminId = 1L
        val bandId = 1L
        val savedBand = BandFixture.createBand("Band 1", 1)
        val bandUpdateRequest = BandUpdateRequest(
            bandId = bandId,
            bandName = "new band name",
        )

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

        //When & Then
        Assertions.assertDoesNotThrow {
            bandService.updateBand(adminId = adminId, bandUpdateRequest = bandUpdateRequest)
        }
    }

    @Test
    fun 밴드_수정_시_유효하지_않은_bandId_값을_입력하면_NOT_FOUND_BAND_예외를_반환한다() {
        // Given
        val adminId = 1L
        val invalidBandId = 2L
        val bandUpdateRequest = BandUpdateRequest(
            bandId = invalidBandId,
            bandName = "new band name",
        )

        `when`(bandRepository.findById(invalidBandId)).thenReturn(Optional.empty())

        // When
        val exception = assertThrows<RestApiException> {
            bandService.updateBand(adminId = adminId, bandUpdateRequest = bandUpdateRequest)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_FOUND_BAND)
    }

    @Test
    fun 밴드_수정_시_유효하지_않은_adminId_값을_입력하면_UNAUTHORIZED_REQUEST_예외를_반환한다() {
        // Given
        val invalidAdminId = 2L
        val bandId = 1L
        val bandUpdateRequest = BandUpdateRequest(
            bandId = bandId,
            bandName = "new band name",
        )

        val savedBand = BandFixture.createBand("Band 1", 1)
        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

        // When
        val exception = assertThrows<RestApiException> {
            bandService.updateBand(adminId = invalidAdminId, bandUpdateRequest = bandUpdateRequest)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_REQUEST)
    }

    @Test
    fun 밴드_수정_시_자신이_생성한_밴드가_아니면_UNAUTHORIZED_BAND_예외를_반환한다() {
        // Given
        val adminId = 1L
        val bandId = 1L
        val bandUpdateRequest = BandUpdateRequest(
            bandId = bandId,
            bandName = "new band name",
        )
        val savedBand = BandFixture.createAnotherUserBand(id = 1, name = "bandName")

        `when`(bandRepository.findById(bandId)).thenReturn(Optional.of(savedBand))

        // When
        val exception = assertThrows<RestApiException> {
            bandService.updateBand(adminId = adminId, bandUpdateRequest = bandUpdateRequest)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNAUTHORIZED_BAND)
    }
}
