package com.wap.wabi.exception

import com.wap.wabi.exception.payload.response.ErrorReason
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val code: String = "",
    val message: String = ""
) : BaseErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "Invalid request."),
    BAD_REQUEST_FILE_NAME_COLUMN(HttpStatus.BAD_REQUEST, "400-11", "파일에 '이름' 칼럼을 확인해주세요."),
    BAD_REQUEST_FILE_STUDENT_ID_COLUMN(HttpStatus.BAD_REQUEST, "400-12", "파일에 '학번' 칼럼을 확인해주세요."),
    BAD_REQUEST_FILE_TYPE(HttpStatus.BAD_REQUEST, "400-13", "파일 형식이 맞지 않습니다.(엑셀, csv)"),
    BAD_REQUEST_STUDENT_ID(HttpStatus.BAD_REQUEST, "400-2", "학번이 형식에 맞지 않습니다."),


    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "401", "Unauthorized."),
    UNAUTHORIZED_CHECK_IN(HttpStatus.UNAUTHORIZED, "401-1", "이벤트에 참여할 수 없는 학생입니다."),
    UNAUTHORIZED_EVENT(HttpStatus.UNAUTHORIZED, "401-2", "이벤트에 대한 권한이 없습니다."),
    UNAUTHORIZED_BAND(HttpStatus.UNAUTHORIZED, "401-3", "밴드에 대한 권한이 없습니다."),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "403", "Forbidden."),
    FORBIDDEN_ACCESS_EVENT_UPDATE(HttpStatus.FORBIDDEN, "403-1", "이벤트 ID가 동일하지 않아 수정할 수 없습니다"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Not found."),
    NOT_FOUND_BAND(HttpStatus.NOT_FOUND, "404-1", "밴드Id를 확인해주세요."),
    NOT_FOUND_EVENT(HttpStatus.NOT_FOUND, "404-2", "이벤트Id를 확인해주세요."),
    NOT_FOUND_STUDENT(HttpStatus.NOT_FOUND, "404-3", "학번을 확인해주세요."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "Not allowed method."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Server error."),

    //EVENT관련
    ALREADY_CHECK_IN(HttpStatus.BAD_REQUEST, "600-1", "이미 체크인 했습니다."),
  
    //BANT관련
    ALREADY_ADD_STUDENT(HttpStatus.BAD_REQUEST, "700-1", "해당 밴드에 이미 학생들이 존재합니다."),

    //Auth 관련
    BAD_REQUEST_ADMIN(HttpStatus.BAD_REQUEST, "800-1","관리자 등록이 형식에 맞지 않습니다."),
    BAD_REQUEST_ADMIN_PASSWORD(HttpStatus.BAD_REQUEST,"800-2","비밀번호가 형식에 맞지 않습니다."),
    BAD_REQUEST_ADMIN_NAME(HttpStatus.BAD_REQUEST,"800-3","이름이 형식에 맞지 않습니다."),
    BAD_REQUEST_EXIST_ADMIN(HttpStatus.BAD_REQUEST, "800-4","이미 존재하는 어드민입니다."),
    BAD_REQUEST_NOT_EXIST_ADMIN(HttpStatus.BAD_REQUEST, "800-5","이미 존재하는 어드민입니다."),
    UNAUTHORIZED_ADMIN(HttpStatus.UNAUTHORIZED, "804-1","올바르지 않은 어드민입니다."),
    UNAUTHORIZED_ADMIN_TOKEN(HttpStatus.UNAUTHORIZED,"804-2","만료된 어드민 정보입니다.");
    override fun getErrorReason(): ErrorReason {
        return ErrorReason(httpStatus, code, message)
    }
}
