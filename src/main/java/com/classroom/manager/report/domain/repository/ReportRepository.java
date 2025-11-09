package com.classroom.manager.report.domain.repository;

import com.classroom.manager.report.domain.Report;
import com.classroom.manager.report.domain.Status;
import com.classroom.manager.report.domain.exception.NotFoundReportException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    default Report getByReportId(Long reportId) {
        return findByReportId(reportId).orElseThrow(NotFoundReportException::new);
    }

    @Query("SELECT r FROM Report r " +
            "JOIN FETCH r.member m " +
            "JOIN FETCH r.classroom c " +
            "WHERE (r.status = :pendingStatus) " +
            "   OR (r.status = :completedStatus AND r.date > :cutoffDate) " +
            "ORDER BY r.date DESC")
    List<Report> findActiveReports(
            @Param("pendingStatus") Status pendingStatus,
            @Param("completedStatus") Status completedStatus,
            @Param("cutoffDate") LocalDateTime cutoffDate
    );

    @Query("SELECT r FROM Report r " +
            "JOIN FETCH r.member m " +
            "JOIN FETCH r.classroom c " +
            "WHERE r.id = :reportId")
    Optional<Report> findByReportId(@Param("reportId") Long reportId);
}
