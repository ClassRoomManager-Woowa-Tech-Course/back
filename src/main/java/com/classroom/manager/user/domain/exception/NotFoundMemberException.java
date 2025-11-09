package com.classroom.manager.user.domain.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
