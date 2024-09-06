package com.wap.wabi.band.service

import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BandService(
    private val bandRepository: BandRepository, private val bandStudentRepository: BandStudentRepository
) {
    @Transactional
    fun getBandStudents(bandId: Long): List<BandStudentData> {
        val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

        val bandStudents = bandStudentRepository.findAllByBand(band)

        return BandStudentData.of(bandStudents)
    }

    @Transactional
    fun createBand(adminId: Long, bandCreateRequest: BandCreateRequest) {
        if (adminId != TEMPORARY_ADMIN_ID) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        val createBand = bandCreateRequest.toBand(adminId)

        bandRepository.save(createBand)
    }

    @Transactional
    fun deleteBand(adminId: Long, bandId: Long) {
        val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

        if (adminId != TEMPORARY_ADMIN_ID) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        if (bandStudentRepository.findAllByBand(band).isNotEmpty()) {
            throw RestApiException(ErrorCode.ALREADY_ADD_STUDENT)
        }

        bandRepository.delete(band)
    }

    companion object {
        private const val TEMPORARY_ADMIN_ID = 1L
    }
}
