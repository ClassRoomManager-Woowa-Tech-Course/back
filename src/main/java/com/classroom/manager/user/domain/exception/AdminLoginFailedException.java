package com.classroom.manager.user.domain.exception;

public class AdminLoginFailedException extends RuntimeException {

    public AdminLoginFailedException() {
        super("비밀번호가 틀렸습니다.");
    }

    public AdminLoginFailedException(String message) {
        super(message);
    }
}
