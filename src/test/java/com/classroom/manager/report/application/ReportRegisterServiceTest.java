package com.classroom.manager.report.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.file.application.FileService;
import com.classroom.manager.file.domain.File;
import com.classroom.manager.file.domain.FileRelatedType;
import com.classroom.manager.report.application.dto.ReportRegisterRequest;
import com.classroom.manager.report.application.dto.ReportUpdateRequest;
import com.classroom.manager.report.domain.Item;
import com.classroom.manager.report.domain.Report;
import com.classroom.manager.report.domain.Status;
import com.classroom.manager.report.domain.repository.ReportRepository;
import com.classroom.manager.report.presentation.dto.ReportResponse;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.Role;
import com.classroom.manager.user.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportRegisterServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ReportRegisterService reportRegisterService;

    private Member testMember;
    private Classroom testClassroom;
    private ReportRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        testMember = mock(Member.class);
        testClassroom = mock(Classroom.class);
        registerRequest = new ReportRegisterRequest(
                Item.MICROPHONE,
                Role.STAFF,
                LocalDateTime.now(),
                "testMemberId",
                "testRoomCode",
                "테스트 신고",
                "내용",
                Status.PENDING
        );
    }

    @Test
    @DisplayName("신고 등록(register) 시, Report 저장과 File 업로드가 호출된다")
    void registerSuccess() {
        List<MultipartFile> files = List.of(
                new MockMultipartFile("file1", "test.jpg", "image/jpeg", "bytes".getBytes())
        );
        Report mockReport = mock(Report.class);
        when(memberRepository.getByMemberId("testMemberId")).thenReturn(testMember);
        when(classroomRepository.getByRoomCode("testRoomCode")).thenReturn(testClassroom);
        when(reportRepository.save(any(Report.class))).thenReturn(mockReport);
        when(mockReport.relatedId()).thenReturn(42L);

        reportRegisterService.register(registerRequest, files);

        verify(reportRepository, times(1)).save(any(Report.class));
        verify(fileService, times(1)).uploadAllFiles(
                42L, FileRelatedType.REPORT, files
        );
    }

    @Test
    @DisplayName("신고 삭제(delete) 시, 파일 삭제 후 Report 삭제가 순서대로 호출된다")
    void deleteSuccess() {
        Long reportId = 42L;

        reportRegisterService.delete(reportId);

        InOrder inOrder = inOrder(fileService, reportRepository);
        inOrder.verify(fileService, times(1)).deleteAllFiles(reportId, FileRelatedType.REPORT);
        inOrder.verify(reportRepository, times(1)).deleteById(reportId);
    }

    @Test
    @DisplayName("신고 목록 조회(findReports) 시, Repository 조회 및 DTO 변환이 수행된다")
    void findReports_Success() {
        List<String> mockUrls = List.of("u1", "u2");
        Report mockReport1 = mock(Report.class);
        Report mockReport2 = mock(Report.class);
        ReportResponse response1 = mock(ReportResponse.class);
        ReportResponse response2 = mock(ReportResponse.class);
        when(mockReport1.to(any())).thenReturn(response1);
        when(mockReport2.to(any())).thenReturn(response2);
        List<Report> mockReports = List.of(mockReport1, mockReport2);
        when(reportRepository.findActiveReports(
                eq(Status.PENDING), eq(Status.COMPLETED), any(LocalDateTime.class)
        )).thenReturn(mockReports);
        when(fileService.findFilesByRelatedIds(any(), eq(FileRelatedType.REPORT))).thenReturn(List.of());
        when(fileService.extractFileUrls(any())).thenReturn(mockUrls);

        List<ReportResponse> results = reportRegisterService.findReports();

        verify(reportRepository, times(1)).findActiveReports(any(), any(), any());
        verify(mockReport1, times(1)).to(any());
        verify(mockReport2, times(1)).to(any());
        assertThat(results).containsExactly(response1, response2);
    }

    @Test
    @DisplayName("신고 수정(update) 시, Report 조회 후 complete() 호출 및 저장이 수행된다")
    void update_Success() {
        ReportUpdateRequest updateRequest = new ReportUpdateRequest(42L, Status.COMPLETED);
        Report mockReport = mock(Report.class);
        when(reportRepository.getByReportId(42L)).thenReturn(mockReport);

        reportRegisterService.update(updateRequest);

        verify(reportRepository, times(1)).getByReportId(42L);
        verify(mockReport, times(1)).complete();
        verify(reportRepository, times(1)).save(mockReport);
    }

    @Test
    @DisplayName("신고 단일 조회(findReport) 시, Report 조회 및 DTO 변환이 수행된다")
    void findReport_Success() {
        Long reportId = 42L;
        List<String> mockUrls = List.of("u1", "u2");
        Report mockReport = mock(Report.class);
        ReportResponse mockResponse = mock(ReportResponse.class);
        when(reportRepository.getByReportId(reportId)).thenReturn(mockReport);
        when(fileService.findFilesByRelatedId(reportId, FileRelatedType.REPORT))
                .thenReturn(List.of());
        when(fileService.extractFileUrls(any()))
                .thenReturn(mockUrls);
        when(mockReport.to(mockUrls)).thenReturn(mockResponse);

        ReportResponse result = reportRegisterService.findReport(reportId);

        verify(reportRepository, times(1)).getByReportId(reportId);
        verify(fileService, times(1)).findFilesByRelatedId(reportId, FileRelatedType.REPORT);
        verify(fileService, times(1)).extractFileUrls(any());
        verify(mockReport, times(1)).to(mockUrls);

        assertThat(result).isEqualTo(mockResponse);
    }
}
