package com.classroom.manager.util;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_CLASSROOM("존재하지 않는 강의실입니다."),
    NOT_FOUND_REPORT("존재하지 않는 신고입니다."),
    NOT_FOUND_GUIDELINE("존재하지 않는 가이드라인입니다."),
    NOT_FOUND_RESERVATION("존재하지 않는 예약입니다."),
    NOT_FOUND_S3_URL("S3 리소스 URL 조회에 실패했습니다."),
    NOT_FOUND_ADMIN_ID("존재하지 않는 관리자 ID입니다"),
    NOT_FOUND_ADMIN("존재하지 않는 관리자입니다"),
    WRONG_PASSWORD("잘못된 비밀번호입니다."),
    UNAUTHORIZED_TOKEN("유효하지 않은 토큰입니다."),
    ALREADY_EXIST_ID("이미 존재하는 관리자 ID입니다."),
    S3_FILE_DELETE_ERROR("S3 파일 삭제 중 오류가 발생했습니다."),
    S3_FILE_UPLOAD_STREAM_ERROR("S3 업로드 중 파일 스트림 오류가 발생했습니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
