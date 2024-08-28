package com.wap.wabi.band.service

import com.opencsv.CSVReader
import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.request.ManualEnrollRequest
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.event.entity.EventStudent
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.entity.Student
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BandService(
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository,
    private val studentRepository: StudentRepository,
    private val fileToBandStudentTranslator: FileToBandStudentTranslator
){

    fun getBandStudents(bandId : Long) : Response{
        val band = bandRepository.findById(bandId).orElseThrow{throw RestApiException(ErrorCode.BAD_REQUEST_BAND)}

        val bandStudents = bandStudentRepository.findAllByBand(band)

        return Response("200", "",  BandStudentData.of(bandStudents))
    }

    fun enrollByFile(bandId: Long, file: MultipartFile): Response {
        val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.BAD_REQUEST_BAND)}

        try {
            val bandStudents = fileToBandStudentTranslator.processFile(file, band)
            bandStudentRepository.saveAll(bandStudents)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Response("200", "", "")
    }

    fun enrollByManual(bandId: Long, request: ManualEnrollRequest): Response {
        val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.BAD_REQUEST_BAND) }

        var bandStudents : MutableList<BandStudent> = mutableListOf()
        request.bandStudentDtos.map {bandStudentDto ->
            val studentId = bandStudentDto.studentId
            val studentName = bandStudentDto.name
            val student = getStudent(studentId, studentName)
            val bandStudent = BandStudent(
                band,
                student,
                bandStudentDto.club,
                bandStudentDto.position,
                bandStudentDto.joinDate,
                bandStudentDto.college,
                bandStudentDto.major,
                bandStudentDto.tel,
                bandStudentDto.academicStatus
            )
            bandStudents.add(bandStudent)
        }
        bandStudentRepository.saveAll(bandStudents)

        return Response("200", "", "");
    }

    fun getStudent(studentId : String, name : String) : Student {
        val student = studentRepository.findById(studentId)
        return if (student.isPresent) student.get() else studentRepository.save(Student(studentId, name))
    }
}