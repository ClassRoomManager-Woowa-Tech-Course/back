package com.classroom.manager.user.application.exception;

import com.classroom.manager.util.ErrorCode;

public class AdminAlreadyExistException extends RuntimeException {

    public AdminAlreadyExistException(String adminId) {
        super(ErrorCode.ALREADY_EXIST_ID.getMessage() + adminId);
    }
}
