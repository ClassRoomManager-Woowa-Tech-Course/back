package com.classroom.manager.reservation.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.reservation.application.dto.ReservationRequest;
import com.classroom.manager.reservation.domain.Reservation;
import com.classroom.manager.reservation.domain.repository.ReservationRepository;
import com.classroom.manager.reservation.presentation.dto.ReservationCancelRequest;
import com.classroom.manager.reservation.presentation.dto.ReservationResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClassroomRepository classroomRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ReservationResponse reservation(ReservationRequest reservationRequest) {
        Classroom classroom = classroomRepository.getByRoomCode(reservationRequest.roomCode());
        Member member = memberRepository.getByMemberId(reservationRequest.memberId());
        log.info("password : {}",reservationRequest.password());
        Reservation reservation = reservationRepository.save(
                Reservation.from(reservationRequest, classroom, member, passwordEncoder));
        return reservation.to();
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

    @Transactional
    public ReservationResponse findReservation(Long reservationId) {
        return reservationRepository.getByReservationId(reservationId).to();
    }

    public void deleteReservation(Long reservationId, ReservationCancelRequest password) {
        Reservation reservation = reservationRepository.getByReservationId(reservationId);
        reservation.validPassword(password.password(), passwordEncoder);
        reservationRepository.deleteById(reservationId);
    }

    public void editReservation(Long reservationId, ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.getByReservationId(reservationId);
        reservation.validPassword(reservationRequest.password(), passwordEncoder);
        Classroom classroom = classroomRepository.getByRoomCode(reservationRequest.roomCode());
        reservation.update(reservationRequest, classroom);
        reservationRepository.save(reservation);
    }
}
