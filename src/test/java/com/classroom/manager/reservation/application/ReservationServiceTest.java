package com.classroom.manager.reservation.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.reservation.application.dto.ReservationRequest;
import com.classroom.manager.reservation.domain.Reservation;
import com.classroom.manager.reservation.domain.repository.ReservationRepository;
import com.classroom.manager.reservation.presentation.dto.ReservationResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.Role;
import com.classroom.manager.user.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ReservationService reservationService;

    private Member testMember;
    private Classroom testClassroom;
    private ReservationRequest testRequest;
    private ReservationResponse testResponse;

    @BeforeEach
    void setUp() {
        testMember = mock(Member.class);
        testClassroom = mock(Classroom.class);
        testRequest = new ReservationRequest(
                "testMemberId",
                "010-1234-5678",
                Role.STAFF,
                "5413",
                "Test Title",
                "Test Purpose",
                LocalDateTime.of(2025, 11, 10, 9, 0),
                LocalDateTime.of(2025, 11, 10, 11, 0),
                "admin_password"
        );
        testResponse = ReservationResponse.builder()
                .reservationId(1L)
                .title("Test Title")
                .build();
    }

    @Test
    @DisplayName("예약 등록(reservation) 시, Member와 Classroom을 조회하고 Reservation을 저장한다")
    void reservation_Success() {
        when(classroomRepository.getByRoomCode("5413")).thenReturn(testClassroom);
        when(memberRepository.getByMemberId("testMemberId")).thenReturn(testMember);
        when(passwordEncoder.encode(any())).thenReturn("admin_password");
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);

        Reservation mockReservation = mock(Reservation.class);
        when(reservationRepository.save(reservationCaptor.capture())).thenReturn(mockReservation);
        when(mockReservation.to()).thenReturn(testResponse);

        ReservationResponse result = reservationService.reservation(testRequest);

        verify(classroomRepository, times(1)).getByRoomCode("5413");
        verify(memberRepository, times(1)).getByMemberId("testMemberId");
        verify(reservationRepository, times(1)).save(reservationCaptor.capture());
        verify(mockReservation, times(1)).to();

        assertThat(result).isEqualTo(testResponse);

        Reservation capturedReservation = reservationCaptor.getValue();
        assertThat(ReflectionTestUtils.getField(capturedReservation, "member")).isEqualTo(testMember);
        assertThat(ReflectionTestUtils.getField(capturedReservation, "classroom")).isEqualTo(testClassroom);
        assertThat(ReflectionTestUtils.getField(capturedReservation, "title")).isEqualTo("Test Title");
        assertThat(ReflectionTestUtils.getField(capturedReservation, "purpose")).isEqualTo("Test Purpose");
    }

    @Test
    @DisplayName("월별 조회 시, Repository를 호출하고 DTO 리스트로 변환하여 반환한다")
    void findReservationByClassroomAndMonth_Success() {
        String roomCode = "5413";
        YearMonth yearMonth = YearMonth.of(2025, 11);
        Reservation mockReservation = mock(Reservation.class);
        when(classroomRepository.getByRoomCode(roomCode)).thenReturn(testClassroom);
        when(reservationRepository.findReservationsForMonth(testClassroom, 2025, 11))
                .thenReturn(List.of(mockReservation));
        when(mockReservation.to()).thenReturn(testResponse);

        List<ReservationResponse> results = reservationService.findReservationByClassroomAndMonth(roomCode, yearMonth);

        verify(classroomRepository, times(1)).getByRoomCode(roomCode);
        verify(reservationRepository, times(1)).findReservationsForMonth(testClassroom, 2025, 11);
        verify(mockReservation, times(1)).to();
        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(testResponse);
    }

    @Test
    @DisplayName("월별 조회 시, 강의실을 찾지 못하면 예외를 던진다")
    void findReservationByClassroomAndMonth_Fail_RoomNotFound() {
        String roomCode = "INVALID_CODE";
        YearMonth yearMonth = YearMonth.of(2025, 11);
        when(classroomRepository.getByRoomCode(roomCode))
                .thenThrow(new EntityNotFoundException("강의실 없음"));

        assertThatThrownBy(() ->
                reservationService.findReservationByClassroomAndMonth(roomCode, yearMonth)
        ).isInstanceOf(EntityNotFoundException.class);

        verify(reservationRepository, never()).findReservationsForMonth(any(), anyInt(), anyInt());
    }
}
