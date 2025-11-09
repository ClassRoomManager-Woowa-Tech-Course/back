package com.classroom.manager.reservation.presentation;

import com.classroom.manager.reservation.application.ReservationService;
import com.classroom.manager.reservation.application.dto.ReservationRequest;
import com.classroom.manager.reservation.presentation.dto.ReservationResponse;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{roomCode}")
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @PathVariable String roomCode,
            @RequestParam YearMonth yearMonth
    ) {
        return ResponseEntity.ok(
                reservationService.findReservationByClassroomAndMonth(roomCode, yearMonth)
        );
    }

    @PostMapping
    public ResponseEntity<Void> reservations(@RequestBody ReservationRequest reservationRequest) {
        reservationService.reservation(reservationRequest);
        return ResponseEntity.ok().build();
    }
}
