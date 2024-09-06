package com.wap.wabi.band.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.payload.request.BandUpdateRequest
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.payload.response.BandsData
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

        if (band.adminId != adminId) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_BAND)
        }

        bandRepository.delete(band)
    }

    @Transactional
    fun getBands(adminId: Long): List<BandsData> {
        if (adminId != TEMPORARY_ADMIN_ID) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        val bands: List<Band> = bandRepository.findAllByAdminId(adminId)
        val bandsDatas: MutableList<BandsData> = mutableListOf()
        bands.forEach { band ->
            bandsDatas.add(BandsData(bandId = band.id, bandName = band.bandName))
        }

        return bandsDatas
    }

    @Transactional
    fun updateBand(adminId: Long, bandUpdateRequest: BandUpdateRequest) {
        val band = bandRepository.findById(bandUpdateRequest.bandId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

        if (adminId != TEMPORARY_ADMIN_ID) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        if (band.adminId != adminId) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_BAND)
        }
    }

    companion object {
        private const val TEMPORARY_ADMIN_ID = 1L
    }
}
