package com.classroom.manager.reservation.domain;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.reservation.application.dto.ReservationRequest;
import com.classroom.manager.reservation.presentation.dto.ReservationResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String title;
    private String purpose;
    private String contact;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static Reservation from(ReservationRequest reservationRequest, Classroom classroom, Member member) {
        return Reservation.builder()
                .member(member)
                .classroom(classroom)
                .role(reservationRequest.role())
                .title(reservationRequest.title())
                .purpose(reservationRequest.purpose())
                .contact(reservationRequest.contact())
                .startDate(reservationRequest.startDate())
                .endDate(reservationRequest.endDate())
                .build();
    }

    public ReservationResponse to() {
        return ReservationResponse.builder()
                .reservationId(id)
                .title(title)
                .date(startDate.toLocalDate())
                .startTime(startDate.toLocalTime())
                .endTime(endDate.toLocalTime())
                .roomCode(classroom.roomCode())
                .memberName(member.name())
                .build();
    }
}
