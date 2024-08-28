package com.wap.wabi.band.service

import com.opencsv.CSVReader
import com.wap.wabi.band.entity.Band
import com.wap.wabi.band.entity.BandStudent
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import com.wap.wabi.student.entity.Student
import com.wap.wabi.student.repository.StudentRepository
import org.apache.poi.ss.usermodel.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate

@Service
class FileToBandStudentTranslator(
    private val studentRepository: StudentRepository,
) {
    fun processFile(file: MultipartFile, band: Band): List<BandStudent> {
        return when {
            file.originalFilename?.endsWith(".csv") == true -> processCsvFile(file, band)
            file.originalFilename?.endsWith(".xlsx") == true || file.originalFilename?.endsWith(".xls") == true -> processExcelFile(file, band)
            else -> throw RestApiException(ErrorCode.BAD_REQUEST_FILE_TYPE)
        }
    }

    private fun processCsvFile(file: MultipartFile, band: Band): List<BandStudent> {
        val reader = CSVReader(InputStreamReader(file.inputStream))
        val headerMap = mutableMapOf<String, Int>()
        val bandStudents = mutableListOf<BandStudent>()

        // 첫 번째 줄을 읽어 헤더를 처리합니다.
        reader.readNext()
        val header = reader.readNext()
        header?.forEachIndexed { index, columnName ->
            headerMap[columnName] = index
        }

        var nextLine: Array<String>?
        while (reader.readNext().also { nextLine = it } != null) {
            val studentId = nextLine!![headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUNM)]
            val studentName = nextLine!![headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUNM)]
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }
            val student = getStudent(studentId, studentName)

            val bandStudent = BandStudent(
                band,
                student,
                headerMap["동아리명"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null },
                headerMap["직책"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null },
                headerMap["가입일자"]?.let {
                    if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it.trim()) } else null
                },
                headerMap["대학"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null },
                headerMap["학부(과)"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null },
                headerMap["연락처"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null },
                headerMap["학적상태"]?.let { if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null }
            )
            bandStudents.add(bandStudent)
        }

        return bandStudents
    }

    private fun processExcelFile(file: MultipartFile, band: Band): List<BandStudent> {
        val workbook: Workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val headerMap = mutableMapOf<String, Int>()
        val bandStudents = mutableListOf<BandStudent>()

        // 첫 번째 행(헤더)을 읽어 컬럼 이름과 인덱스를 매핑합니다.
        val headerRow = sheet.getRow(1) // 1번째 줄이 헤더인 경우, 1로 지정
        for (cell in headerRow) {
            headerMap[cell.stringCellValue] = cell.columnIndex
        }

        for (row in sheet) {
            if (row.rowNum <= 1) continue

            val studentId = getCellValueAsString(row.getCell(headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUNM)))
            val studentName = getCellValueAsString(row.getCell(headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUNM)))
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }
            val student = getStudent(studentId, studentName)

            val bandStudent = BandStudent(
                band,
                student,
                headerMap["동아리명"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                },
                headerMap["직책"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                },
                headerMap["가입일자"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it.trim()) }
                },
                headerMap["대학"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                },
                headerMap["학부(과)"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                },
                headerMap["연락처"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                },
                headerMap["학적상태"]?.let {
                    getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
                }
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
        return if (student.isPresent) student.get() else studentRepository.save(Student(studentId, name))
    }
}