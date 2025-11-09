package com.classroom.manager.reservation.application.dto;

import com.classroom.manager.user.domain.Role;
import java.time.LocalDateTime;

public record ReservationRequest (
        Role role,
        String memberId,
        String contact,
        String roomCode,
        String title,
        String purpose,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
