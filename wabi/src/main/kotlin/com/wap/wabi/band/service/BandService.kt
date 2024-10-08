package com.wap.wabi.band.service

import com.wap.wabi.auth.admin.repository.AdminRepository
import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.payload.request.BandCreateRequest
import com.wap.wabi.band.payload.request.BandUpdateRequest
import com.wap.wabi.band.payload.response.BandDetailData
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.payload.response.BandsData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentBandNameRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BandService(
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository,
    private val adminRepository: AdminRepository,
    private val eventStudentBandNameRepository: EventStudentBandNameRepository,
    private val eventBandRepository: EventBandRepository,
    private val eventRepository: EventRepository,
    private val eventStudentRepository: EventStudentRepository
) {
    @Transactional
    fun getBandStudents(bandId: Long): List<BandStudentData> {
        val band = bandRepository.findById(bandId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }

        val bandStudents = bandStudentRepository.findAllByBand(band)

        return BandStudentData.of(bandStudents)
    }

    @Transactional
    fun createBand(adminId: Long, bandCreateRequest: BandCreateRequest) {
        val admin = adminRepository.findById(adminId)

        if (admin.isEmpty) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        val createBand = bandCreateRequest.toBand(admin.get().id)

        bandRepository.save(createBand)
    }

    @Transactional
    fun deleteBand(adminId: Long, bandId: Long) {
        val band = bandRepository.findById(bandId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }
        val admin = adminRepository.findById(adminId)

        if (admin.isEmpty) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        if (bandStudentRepository.findAllByBand(band).isNotEmpty()) {
            throw RestApiException(ErrorCode.ALREADY_ADD_STUDENT)
        }

        if (band.adminId != admin.get().id) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_BAND)
        }

        val eventBands = eventBandRepository.findAllByBand(band)
        eventBands.forEach { eventBand ->
            deleteEventByBand(event = eventBand.event)
        }
        bandStudentRepository.deleteAllByBand(band)
        bandRepository.delete(band)
    }

    private fun deleteEventByBand(event: Event) {
        val eventStudens = eventStudentRepository.findAllByEvent(event)

        eventStudens.forEach { eventStudent ->
            eventStudentBandNameRepository.deleteByEventStudent(eventStudent)
        }

        eventStudentRepository.deleteByEvent(event)
        eventBandRepository.deleteByEvent(event)
        eventRepository.delete(event)
    }

    @Transactional
    fun getBands(adminId: Long): List<BandsData> {
        val admin = adminRepository.findById(adminId)

        if (admin.isEmpty) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        val bands: List<Band> = bandRepository.findAllByAdminId(admin.get().id)
        val bandsDatas: MutableList<BandsData> = mutableListOf()
        bands.forEach { band ->
            bandsDatas.add(BandsData(bandId = band.id, bandName = band.bandName))
        }

        return bandsDatas
    }

    @Transactional
    fun updateBand(adminId: Long, bandUpdateRequest: BandUpdateRequest) {
        val band = bandRepository.findById(bandUpdateRequest.bandId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }
        val admin = adminRepository.findById(adminId)

        if (admin.isEmpty) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_REQUEST)
        }

        if (band.adminId != admin.get().id) {
            throw RestApiException(ErrorCode.UNAUTHORIZED_BAND)
        }

        band.update(bandUpdateRequest)
    }

    @Transactional
    fun getBandDetail(bandId: Long): BandDetailData {
        val band = bandRepository.findById(bandId)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }

        return BandDetailData.of(band = band)
    }
}
