package com.classroom.manager.user.domain.exception;

public class NotFoundAdminException extends RuntimeException {

    public NotFoundAdminException() {
        super("존재하지 않는 관리자입니다.");
    }
}
