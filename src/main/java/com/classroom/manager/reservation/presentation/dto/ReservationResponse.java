package com.classroom.manager.reservation.presentation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record ReservationResponse(
        Long reservationId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String roomCode,
        String title,
        String memberName
) {
}
