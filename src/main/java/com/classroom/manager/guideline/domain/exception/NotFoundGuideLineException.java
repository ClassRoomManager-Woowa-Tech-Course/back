package com.classroom.manager.guideline.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class NotFoundGuideLineException extends RuntimeException {

    public NotFoundGuideLineException() {
        super(ErrorCode.NOT_FOUND_GUIDELINE.getMessage());
    }
}
