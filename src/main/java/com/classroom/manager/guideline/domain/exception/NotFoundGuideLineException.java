package com.classroom.manager.guideline.domain.exception;

public class NotFoundGuideLineException extends RuntimeException {

    public NotFoundGuideLineException() {
        super("존재하지 않는 가이드라인입니다.");
    }
}
