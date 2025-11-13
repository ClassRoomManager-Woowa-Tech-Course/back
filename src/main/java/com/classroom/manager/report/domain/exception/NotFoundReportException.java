package com.classroom.manager.report.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class NotFoundReportException extends RuntimeException {

    public NotFoundReportException() {
        super(ErrorCode.NOT_FOUND_REPORT.getMessage());
    }
}
