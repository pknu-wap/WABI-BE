package com.wap.wabi.band.service

import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BandService(
	private val bandRepository : BandRepository, private val bandStudentRepository : BandStudentRepository
) {
	@Transactional
	fun getBandStudents(bandId : Long) : List<BandStudentData> {
		val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

		val bandStudents = bandStudentRepository.findAllByBand(band)

		return BandStudentData.of(bandStudents)
	}
}
