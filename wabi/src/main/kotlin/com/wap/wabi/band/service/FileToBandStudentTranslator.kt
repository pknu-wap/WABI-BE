package com.wap.wabi.band.service

import com.opencsv.CSVReader
import com.wap.wabi.band.payload.BandStudentDto
import com.wap.wabi.exception.ErrorCode
import com.wap.wabi.exception.RestApiException
import org.apache.poi.ss.usermodel.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.LocalDate

@Service
class FileToBandStudentTranslator(
) {
    fun translateFileToDto(file: MultipartFile): List<BandStudentDto> {
        return when {
            file.originalFilename?.endsWith(".csv") == true -> processCsvFile(file)
            file.originalFilename?.endsWith(".xlsx") == true || file.originalFilename?.endsWith(".xls") == true -> processExcelFile(file)
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

        var nextLine: Array<String>?
        while (reader.readNext().also { nextLine = it } != null) {
            val studentId = nextLine!![headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUNM)]
            val studentName = nextLine!![headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUNM)]
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }

            val clubName = headerMap["동아리명"]?.let {
                if (it >= 0) nextLine?.get(it)?.takeIf { it.isNotBlank() } else null
            }
            if (clubName.isNullOrBlank()) {
                continue
            }

            val bandStudentDto = BandStudentDto(
                studentId,
                studentName,
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

            val studentId = getCellValueAsString(row.getCell(headerMap["학번"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_STUDENT_ID_COLUNM)))
            val studentName = getCellValueAsString(row.getCell(headerMap["성명"] ?: throw RestApiException(ErrorCode.BAD_REQUEST_FILE_NAME_COLUNM)))
            if (studentId.isNullOrBlank() || studentName.isNullOrBlank()) {
                continue
            }
            val clubName = headerMap["동아리명"]?.let {
                getCellValueAsString(row.getCell(it))?.takeIf { it.isNotBlank() }
            }
            if (clubName.isNullOrBlank()) {
                continue
            }

            val bandStudentDto = BandStudentDto(
                studentId,
                studentName,
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