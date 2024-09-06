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
        if(adminId != TEMPORARY_ADMIN_ID){
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }
    }

    companion object {
        private const val TEMPORARY_ADMIN_ID = 1L
    }
}
