package com.classroom.manager.user.infra.security.exception;

import com.classroom.manager.util.ErrorCode;

public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException(String token) {
        super(ErrorCode.UNAUTHORIZED_TOKEN.getMessage() + ": " + token);
    }
}
