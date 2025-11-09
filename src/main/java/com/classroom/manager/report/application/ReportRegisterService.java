package com.classroom.manager.report.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.file.application.FileService;
import com.classroom.manager.file.domain.File;
import com.classroom.manager.file.domain.FileRelatedType;
import com.classroom.manager.report.application.dto.ReportRegisterRequest;
import com.classroom.manager.report.application.dto.ReportUpdateRequest;
import com.classroom.manager.report.domain.Report;
import com.classroom.manager.report.domain.Status;
import com.classroom.manager.report.domain.repository.ReportRepository;
import com.classroom.manager.report.presentation.dto.ReportResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportRegisterService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ClassroomRepository classroomRepository;
    private final FileService fileService;

    public void register(ReportRegisterRequest reportRegisterRequest, List<MultipartFile> files) {
        Member member = memberRepository.getByMemberId(reportRegisterRequest.memberId());
        Classroom classroom = classroomRepository.getByRoomCode(reportRegisterRequest.roomCode());
        Report report = reportRepository.save(Report.from(reportRegisterRequest, member, classroom));
        fileService.uploadAllFiles(report.relatedId(), FileRelatedType.REPORT, files);
    }

    public void delete(Long reportId) {
        List<File> files = fileService.findFilesByReportIdAndRelatedType(reportId, FileRelatedType.REPORT);
        fileService.deleteAllFiles(files);
        reportRepository.deleteById(reportId);
    }

    public List<ReportResponse> findReports() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<Report> reports = reportRepository.findActiveReports(Status.PENDING, Status.COMPLETED, cutoffDate);
        List<ReportResponse> reportResponses = new ArrayList<>();
        return reports.stream()
                .map(Report::to)
                .toList();
    }

    public void update(ReportUpdateRequest reportUpdateRequest) {
        Report report = reportRepository.getByReportId(reportUpdateRequest.reportId());
        report.complete();
        reportRepository.save(report);
    }

    public ReportResponse findReport(Long reportId) {
        Report report = reportRepository.getByReportId(reportId);
        return report.to();
    }
}
