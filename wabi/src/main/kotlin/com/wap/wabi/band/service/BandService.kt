package com.wap.wabi.band.service

import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.common.payload.response.Response
import org.springframework.stereotype.Service

@Service
class BandService(
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository
){

    fun getBandStudents(bandId : Long) : Response{
        val band = bandRepository.findById(bandId).orElseThrow{throw IllegalArgumentException("no band")}

        val bandStudents = bandStudentRepository.findAllByBand(band)

        return Response("200", "",  BandStudentData.of(bandStudents))
    }
}