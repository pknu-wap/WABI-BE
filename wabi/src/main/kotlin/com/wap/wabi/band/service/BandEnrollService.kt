package com.wap.wabi.band.service

import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.band.payload.request.EnrollRequest
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
class BandEnrollService(
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
        val request = EnrollRequest(bandStudentDtos)
        return enrollBandStudent(bandId, request)
    }

    @Transactional
    fun enrollBandStudent(bandId: Long, request: EnrollRequest): Long {
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

            val bandStudent = BandStudent.builder()
                .band(band)
                .student(student)
                .club(bandStudentDto.club)
                .position(bandStudentDto.position)
                .joinDate(bandStudentDto.joinDate)
                .college(bandStudentDto.college)
                .major(bandStudentDto.major)
                .tel(bandStudentDto.tel)
                .academicStatus(bandStudentDto.academicStatus)
                .build()

            if (!alreadyHasSameStudentInBand(student, band)) bandStudents.add(bandStudent)
        }
        bandStudentRepository.saveAll(bandStudents)

        return bandId;
    }

    private fun alreadyHasSameStudentInBand(student: Student, band: Band): Boolean {
        return bandStudentRepository.findByBandAndStudent(band, student).isPresent
    }
}
