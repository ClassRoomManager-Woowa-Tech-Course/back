package com.classroom.manager.reservation.domain.exception;

public class NotFoundReservationException extends RuntimeException{

    public NotFoundReservationException() {
        super("예약이 존재하지 않습니다.");
    }
}
