package com.classroom.manager.reservation.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD.getMessage());
    }
}
