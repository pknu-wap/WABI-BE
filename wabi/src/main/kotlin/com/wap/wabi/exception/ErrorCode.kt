package com.wap.wabi.exception
enum class ErrorCode (val code: String = "", val message: String = ""){
    BAD_REQUEST("400", "Invalid request."),
    BAD_REQUEST_FILE_NAME_COLUNM("400-11", "파일에 '이름' 칼럼을 확인해주세요."),
    BAD_REQUEST_FILE_STUDENT_ID_COLUNM("400-12", "파일에 '학번' 칼럼을 확인해주세요."),
    BAD_REQUEST_FILE_TYPE("400-13", "파일 형식이 맞지 않습니다.(엑셀, csv)"),

    UNAUTHORIZED_REQUEST("401", "Unauthorized."),
    UNAUTHORIZED_CHECK_IN("401-1","이벤트에 참여할 수 없는 학생입니다."),

    FORBIDDEN_ACCESS("403", "Forbidden."),

    NOT_FOUND("404", "Not found."),
    BAD_REQUEST_BAND("404-1", "밴드Id를 확인해주세요."),
    BAD_REQUEST_EVENT("404-2", "이벤트Id를 확인해주세요."),
    BAD_REQUEST_STUDENT("404-3","학번을 확인해주세요."),

    METHOD_NOT_ALLOWED("405", "Not allowed method."),

    INTERNAL_SERVER_ERROR("500", "Server error."),

    ALREADY_CHECK_IN("600-1","이미 체크인 했습니다."),

}
