package com.wap.wabi.band.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.band.payload.request.BandStudentEnrollRequest
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.entity.Student
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BandCommandService(
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository,
    private val studentRepository: StudentRepository,
    private val fileToBandStudentTranslator: FileToBandStudentTranslator
) {
    fun enrollByFile(bandId: Long, file: MultipartFile): Long {
        val bandStudentDtos = fileToBandStudentTranslator.translateFileToDto(file)
        return enrollByDto(bandId, bandStudentDtos)
    }

    @Transactional
    fun enrollByDto(bandId: Long, bandStudentDtos: List<BandStudentDto>): Long {
        val request = BandStudentEnrollRequest(bandStudentDtos)
        return enrollBandStudent(bandId, request)
    }

    @Transactional
    fun enrollBandStudent(bandId: Long, request: BandStudentEnrollRequest): Long {
        val band = bandRepository.findById(bandId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }

        val bandStudents: MutableList<BandStudent> = mutableListOf()
        request.bandStudentDtos.forEach { bandStudentDto ->
            val studentId = bandStudentDto.studentId
            val studentName = bandStudentDto.name
            val student = studentRepository.findById(studentId).orElseGet {
                studentRepository.save(
                    Student.builder()
                        .id(studentId)
                        .name(studentName)
                        .build()
                )
            }

            val bandStudent = buildBandStudent(band, student, bandStudentDto)

            if (!alreadyHasSameStudentInBand(student, band)) bandStudents.add(bandStudent)
        }
        bandStudentRepository.saveAll(bandStudents)

        return bandId;
    }

    private fun alreadyHasSameStudentInBand(student: Student, band: Band): Boolean {
        return bandStudentRepository.findByBandAndStudent(band, student).isPresent
    }

    @Transactional
    fun deleteBandStudent(bandId: Long, studentId: String) {
        val band = bandRepository.findById(bandId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }
        val student =
            studentRepository.findById(studentId).orElseThrow() { RestApiException(ErrorCode.NOT_FOUND_STUDENT) }
        bandStudentRepository.deleteBandStudentByBandAndStudent(band, student)
    }

    @Transactional
    fun updateBandStudent(bandId: Long, request: BandStudentDto): Long {
        val band = bandRepository.findById(bandId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_BAND) }
        val student =
            studentRepository.findById(request.studentId).orElseThrow { RestApiException(ErrorCode.NOT_FOUND_STUDENT) }

        val bandStudent = bandStudentRepository.findByBandAndStudent(band, student)
            .orElseThrow { RestApiException(ErrorCode.NOT_FOUND_STUDENT) }

        bandStudent.update(request)

        return bandId
    }

    private fun buildBandStudent(band: Band, student: Student, request: BandStudentDto): BandStudent {
        val bandStudent = BandStudent.builder()
            .band(band)
            .student(student)
            .club(request.club)
            .position(request.position)
            .joinDate(request.joinDate)
            .college(request.college)
            .major(request.major)
            .tel(request.tel)
            .academicStatus(request.academicStatus)
            .build()
        return bandStudent
    }
}
