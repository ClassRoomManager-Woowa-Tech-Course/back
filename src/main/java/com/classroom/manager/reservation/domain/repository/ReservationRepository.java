package com.classroom.manager.reservation.domain.repository;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.reservation.domain.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.member m " +
            "JOIN FETCH r.classroom c " +
            "WHERE r.classroom = :classroom " +
            "AND YEAR(r.startDate) = :year " +
            "AND MONTH(r.startDate) = :month " +
            "ORDER BY r.startDate ASC")
    List<Reservation> findReservationsForMonth(
            @Param("classroom") Classroom classroom,
            @Param("year") int year,
            @Param("month") int month
    );
}
