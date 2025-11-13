package com.classroom.manager.classroom.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class NotFoundClassroomException extends RuntimeException {

    public NotFoundClassroomException() {
        super(ErrorCode.NOT_FOUND_CLASSROOM.getMessage());
    }
}
