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
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        fileService.deleteAllFiles(reportId, FileRelatedType.REPORT);
        reportRepository.deleteById(reportId);
    }

    public List<ReportResponse> findReports() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<Report> reports = reportRepository.findActiveReports(Status.PENDING, Status.COMPLETED, cutoffDate);
        List<Long> reportIds = reports.stream().map(Report::relatedId).toList();
        List<File> allFiles = fileService.findFilesByRelatedIds(reportIds, FileRelatedType.REPORT);
        Map<Long, List<File>> filesMap = allFiles.stream()
                .collect(Collectors.groupingBy(File::relatedId));
        return reports.stream()
                .map(report -> {
                    List<File> files = filesMap.getOrDefault(report.relatedId(), List.of());
                    return convertToResponse(report, files);
                })
                .toList();
    }

    public void update(ReportUpdateRequest reportUpdateRequest) {
        Report report = reportRepository.getByReportId(reportUpdateRequest.reportId());
        report.complete();
        reportRepository.save(report);
    }

    public ReportResponse findReport(Long reportId) {
        Report report = reportRepository.getByReportId(reportId);
        List<File> files = fileService.findFilesByRelatedId(reportId,
                FileRelatedType.REPORT);
        return convertToResponse(report, files);
    }

    private ReportResponse convertToResponse(Report report, List<File> files) {
        List<String> urls = fileService.extractFileUrls(files);
        return report.to(urls);
    }
}
