package com.classroom.manager.classroom.domain.exception;

public class NotFoundClassroomException extends RuntimeException {

    public NotFoundClassroomException() {
        super("존재하지 않는 강의실입니다.");
    }
}
