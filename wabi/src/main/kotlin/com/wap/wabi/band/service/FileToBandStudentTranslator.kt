package com.wap.wabi.band.service

import com.opencsv.CSVReader
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate

@Component
class FileToBandStudentTranslator(
) {
    fun translateFileToDto(file: MultipartFile): List<BandStudentDto> {
        val originalFilename = file.originalFilename ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_TYPE)
        return when {
            originalFilename.endsWith(".csv") -> processCsvFile(file)
            originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls") -> processExcelFile(file)
            else -> throw RestApiException(ErrorCode.BAD_REQUEST_FILE_TYPE)
        }

    }

    private fun processCsvFile(file: MultipartFile): List<BandStudentDto> {
        val reader = CSVReader(InputStreamReader(file.inputStream))
        val headerMap = mutableMapOf<String, Int>()
        val bandStudentDtos = mutableListOf<BandStudentDto>()

        // 첫 번째 줄을 읽어 헤더를 처리합니다.
        reader.readNext()
        val header = reader.readNext()
        header?.forEachIndexed { index, columnName ->
            headerMap[columnName] = index
        }

        for (nextLine in reader) {
            val studentId =
                nextLine!![headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUMN)]
            val studentName =
                nextLine!![headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUMN)]
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }

            val clubName = headerMap["동아리명"]?.let {
                if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null
            }
            if (clubName.isNullOrBlank()) {
                continue
            }

            val bandStudentDto = BandStudentDto(studentId, studentName, headerMap["동아리명"]?.let {
                nextLine?.get(it)
            }, headerMap["직책"]?.let {
                nextLine?.get(it)
            }, headerMap["가입일자"]?.let {
                if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it.trim()) } else null
            }, headerMap["대학"]?.let {
                nextLine?.get(it)
            }, headerMap["학부(과)"]?.let {
                nextLine?.get(it)
            }, headerMap["연락처"]?.let {
                nextLine?.get(it)
            }, headerMap["학적상태"]?.let {
                nextLine?.get(it)
            })
            bandStudentDtos.add(bandStudentDto)
        }

        return bandStudentDtos
    }

    private fun processExcelFile(file: MultipartFile): List<BandStudentDto> {
        val workbook: Workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val headerMap = mutableMapOf<String, Int>()
        val bandStudentDtos = mutableListOf<BandStudentDto>()

        // 첫 번째 행(헤더)을 읽어 컬럼 이름과 인덱스를 매핑합니다.
        val headerRow = sheet.getRow(1) // 1번째 줄이 헤더인 경우, 1로 지정
        for (cell in headerRow) {
            headerMap[cell.stringCellValue] = cell.columnIndex
        }

        for (row in sheet) {
            if (row.rowNum <= 1) continue

            val studentId = getCellValueAsString(
                row.getCell(
                    headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUMN)
                )
            )
            val studentName = getCellValueAsString(
                row.getCell(
                    headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUMN)
                )
            )
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }

            val bandStudentDto = BandStudentDto(studentId, studentName, headerMap["동아리명"]?.let {
                getCellValueAsString(row.getCell(it))
            }, headerMap["직책"]?.let {
                getCellValueAsString(row.getCell(it))
            }, headerMap["가입일자"]?.let {
                getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it.trim()) }
            }, headerMap["대학"]?.let {
                getCellValueAsString(row.getCell(it))
            }, headerMap["학부(과)"]?.let {
                getCellValueAsString(row.getCell(it))
            }, headerMap["연락처"]?.let {
                getCellValueAsString(row.getCell(it))
            }, headerMap["학적상태"]?.let {
                getCellValueAsString(row.getCell(it))
            })

            bandStudentDtos.add(bandStudentDto)
        }

        workbook.close()

        return bandStudentDtos
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
}
