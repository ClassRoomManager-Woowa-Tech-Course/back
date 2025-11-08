package com.classroom.manager.user.application.exception;

public class AdminAlreadyExistException extends RuntimeException {

    public AdminAlreadyExistException(String adminId) {
        super("이미 존재하는 관리자 ID입니다: " + adminId);
    }
}
