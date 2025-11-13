package com.classroom.manager.user.application.exception;

import com.classroom.manager.util.ErrorCode;

public class AdminNotFoundException extends RuntimeException {

    public AdminNotFoundException(String adminId) {
        super(ErrorCode.NOT_FOUND_ADMIN_ID.getMessage() + adminId);
    }
}
