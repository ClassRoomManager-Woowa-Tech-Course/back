package com.classroom.manager.report.domain.exception;

public class NotFoundReportException extends RuntimeException {

    public NotFoundReportException() {
        super("신고건을 찾지 못했습니다.");
    }
}
