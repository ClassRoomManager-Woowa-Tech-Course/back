package com.classroom.manager.user.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class NotFoundAdminException extends RuntimeException {

    public NotFoundAdminException() {
        super(ErrorCode.NOT_FOUND_ADMIN.getMessage());
    }
}
