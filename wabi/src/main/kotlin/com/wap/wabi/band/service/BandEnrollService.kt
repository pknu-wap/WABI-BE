package com.wap.wabi.band.service

import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.request.EnrollRequest
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.entity.Student
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BandEnrollService(
	private val bandRepository : BandRepository,
	private val bandStudentRepository : BandStudentRepository,
	private val studentRepository : StudentRepository,
	private val fileToBandStudentTranslator : FileToBandStudentTranslator
) {
	fun enrollByFile(bandId : Long, file : MultipartFile) : Long {
		val request = EnrollRequest(fileToBandStudentTranslator.translateFileToDto(file))
		return enrollBandStudent(bandId, request)
	}

	fun enrollBandStudent(bandId : Long, request : EnrollRequest) : Long {
		val band = bandRepository.findById(bandId).orElseThrow { throw RestApiException(ErrorCode.NOT_FOUND_BAND) }

		var bandStudents : MutableList<BandStudent> = mutableListOf()
		request.bandStudentDtos.map { bandStudentDto ->
			val studentId = bandStudentDto.studentId
			val studentName = bandStudentDto.name
			val student = getStudent(studentId, studentName)

			val bandStudent = BandStudent(
				band, student, bandStudentDto.club, bandStudentDto.position, bandStudentDto.joinDate, bandStudentDto.college, bandStudentDto.major, bandStudentDto.tel, bandStudentDto.academicStatus
			)

			if(!alreadyHasBandStudent(bandStudentDto.club.toString(), student) && !containsSameStudentIdAndClub(
					bandStudents, bandStudentDto.club.toString(), student
				)
			) bandStudents.add(bandStudent)

		}
		bandStudentRepository.saveAll(bandStudents)

		return bandId;
	}

	fun containsSameStudentIdAndClub(list : List<BandStudent>, club : String, student : Student) : Boolean {
		return list.any { it.club == club && it.student == student }
	}

	fun alreadyHasBandStudent(club : String, student : Student) : Boolean {
		val bandStudent = bandStudentRepository.findByClubAndStudent(club, student)
		return bandStudent.isPresent
	}

	fun getStudent(studentId : String, name : String) : Student {
		val student = studentRepository.findById(studentId)
		return if(student.isPresent) student.get() else studentRepository.save(Student(studentId, name))
	}
}
