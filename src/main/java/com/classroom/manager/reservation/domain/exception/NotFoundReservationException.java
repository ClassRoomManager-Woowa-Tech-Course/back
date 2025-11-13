package com.classroom.manager.reservation.domain.exception;

import com.classroom.manager.util.ErrorCode;

public class NotFoundReservationException extends RuntimeException{

    public NotFoundReservationException() {
        super(ErrorCode.NOT_FOUND_RESERVATION.getMessage());
    }
}
