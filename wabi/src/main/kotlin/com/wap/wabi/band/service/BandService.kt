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
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate

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

    fun enrollByFile(bandId: Long, file: MultipartFile): Response {
        val band = bandRepository.findById(bandId).orElseThrow { throw IllegalArgumentException("no band") }

        try {
            val bandStudents = processFile(file, band)
            bandStudentRepository.saveAll(bandStudents)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Response("200", "", "")
    }

    private fun processFile(file: MultipartFile, band:Band): List<BandStudent> {
        return when {
            file.originalFilename?.endsWith(".csv") == true -> processCsvFile(file, band)
            file.originalFilename?.endsWith(".xlsx") == true || file.originalFilename?.endsWith(".xls") == true -> processExcelFile(file, band)
            else -> throw IllegalArgumentException("Unsupported file type")
        }
    }

    private fun processCsvFile(file: MultipartFile, band:Band): List<BandStudent> {
        val reader = CSVReader(InputStreamReader(file.inputStream))
        val bandStudents = mutableListOf<BandStudent>()
        var nextLine: Array<String>?

        while (reader.readNext().also { nextLine = it } != null) {
            val student = getStudent(nextLine!![5], nextLine!![6])
            val bandStudent = BandStudent(
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

        return bandStudents
    }

    private fun processExcelFile(file: MultipartFile, band: Band): List<BandStudent> {
        val workbook: Workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val bandStudents = mutableListOf<BandStudent>()

        for (row in sheet) {
            if (row.rowNum == 0) continue

            val student = getStudent(
                getCellValueAsString(row.getCell(5)),
                getCellValueAsString(row.getCell(6))
            )

            val bandStudent = BandStudent(
                band,
                student,
                getCellValueAsString(row.getCell(1)),
                getCellValueAsString(row.getCell(2)),
                if (getCellValueAsString(row.getCell(3)).isBlank()) null else LocalDate.parse(getCellValueAsString(row.getCell(3)).trim()),
                getCellValueAsString(row.getCell(7)),
                getCellValueAsString(row.getCell(8)),
                getCellValueAsString(row.getCell(9)),
                getCellValueAsString(row.getCell(10))
            )
            bandStudents.add(bandStudent)
        }

        workbook.close()

        return bandStudents
    }

    private fun getCellValueAsString(cell: Cell?): String {
        return when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> if (DateUtil.isCellDateFormatted(cell)) {
                cell.localDateTimeCellValue.toLocalDate().toString()
            } else {
                cell.numericCellValue.toBigDecimal().toPlainString()
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> cell.cellFormula
            else -> ""
        }
    }

    fun getStudent(studentId : String, name : String) : Student {
        val student = studentRepository.findById(studentId)
        return if (student.isPresent) student.get() else makeStudent(studentId,name)
    }

    fun makeStudent(studentId : String, name : String) : Student{
        return studentRepository.save(Student(studentId, name))
    }
}