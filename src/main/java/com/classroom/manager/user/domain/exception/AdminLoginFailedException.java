package com.classroom.manager.user.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class AdminLoginFailedException extends RuntimeException {

    public AdminLoginFailedException() {
        super(ErrorCode.WRONG_PASSWORD.getMessage());
    }

    public AdminLoginFailedException(String message) {
        super(message);
    }
}
