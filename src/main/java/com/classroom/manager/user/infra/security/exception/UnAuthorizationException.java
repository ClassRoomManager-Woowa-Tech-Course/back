package com.classroom.manager.user.infra.security.exception;

public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException(String token) {
        super("유효하지 않은 토큰입니다: " + token);
    }
}
