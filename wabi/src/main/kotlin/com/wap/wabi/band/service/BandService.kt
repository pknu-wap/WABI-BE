package com.wap.wabi.band.service

import com.opencsv.CSVReader
import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.band.payload.response.BandStudentData
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.common.payload.response.Response
import com.wap.wabi.student.entity.Student
import com.wap.wabi.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BandService(
    private val bandRepository: BandRepository,
    private val bandStudentRepository: BandStudentRepository,
    private val studentRepository: StudentRepository
){

    fun getBandStudents(bandId : Long) : Response{
        val band = bandRepository.findById(bandId).orElseThrow{throw IllegalArgumentException("no band")}

        val bandStudents = bandStudentRepository.findAllByBand(band)

        return Response("200", "",  BandStudentData.of(bandStudents))
    }

    fun enrollByFile(bandId : Long, file : MultipartFile) : Response{
        val band = bandRepository.findById(bandId).orElseThrow{throw IllegalArgumentException("no band")}

        try {
            val reader = CSVReader(InputStreamReader(file.inputStream))
            val bandStudents = mutableListOf<BandStudent>()
            var nextLine: Array<String>?

            while (reader.readNext().also { nextLine = it } != null) {
                val student = getStudent(nextLine!![5], nextLine!![6])
                val bandStudent = BandStudent (
                    band,
                    student,
                    nextLine!![1],
                    nextLine!![2],
                    if (nextLine!![3].isBlank()) null else LocalDate.parse(nextLine!![3].trim()),
                    nextLine!![7],
                    nextLine!![8],
                    nextLine!![9],
                    nextLine!![10]
                )

                bandStudents.add(bandStudent)
            }

            bandStudentRepository.saveAll(bandStudents)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Response("200", "", "")
    }

    fun getStudent(studentId : String, name : String) : Student {
        val student = studentRepository.findById(studentId)
        return if (student.isPresent) student.get() else makeStudent(studentId,name)
    }

    fun makeStudent(studentId : String, name : String) : Student{
        return studentRepository.save(Student(studentId, name))
    }
}