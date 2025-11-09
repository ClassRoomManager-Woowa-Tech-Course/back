package com.classroom.manager.reservation.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.reservation.application.dto.ReservationRequest;
import com.classroom.manager.reservation.domain.Reservation;
import com.classroom.manager.reservation.domain.repository.ReservationRepository;
import com.classroom.manager.reservation.presentation.dto.ReservationResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.repository.MemberRepository;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClassroomRepository classroomRepository;
    private final MemberRepository memberRepository;

    public void reservation(ReservationRequest reservationRequest) {
        Classroom classroom = classroomRepository.getByRoomCode(reservationRequest.roomCode());
        Member member = memberRepository.getByMemberId(reservationRequest.memberId());
        reservationRepository.save(Reservation.from(reservationRequest, classroom, member));
    }

    public List<ReservationResponse> findReservationByClassroomAndMonth(String roomCode, YearMonth yearMonth) {
        Classroom classroom = classroomRepository.getByRoomCode(roomCode);
        List<Reservation> reservations = reservationRepository.findReservationsForMonth(
                classroom,
                yearMonth.getYear(),
                yearMonth.getMonthValue()
        );
        return reservations.stream().map(Reservation::to).toList();
    }
}
