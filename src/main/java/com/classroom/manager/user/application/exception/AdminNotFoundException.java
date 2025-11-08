package com.classroom.manager.user.application.exception;

public class AdminNotFoundException extends RuntimeException {

    public AdminNotFoundException(String adminId) {
        super("존재하지 않는 관리자 ID입니다: " + adminId);
    }
}
