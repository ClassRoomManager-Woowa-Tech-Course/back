package com.classroom.manager.report.presentation;

import com.classroom.manager.report.application.ReportRegisterService;
import com.classroom.manager.report.application.dto.ReportRegisterRequest;
import com.classroom.manager.report.application.dto.ReportUpdateRequest;
import com.classroom.manager.report.presentation.dto.ReportResponse;
import com.classroom.manager.user.infra.security.AdminAuthorizationValidator;
import com.classroom.manager.user.infra.security.annotation.Auth;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRegisterService reportRegisterService;
    private final AdminAuthorizationValidator adminAuthorizationValidator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerReport(
            @RequestPart("report") ReportRegisterRequest reportRegisterRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        reportRegisterService.register(reportRegisterRequest, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports(@Auth TokenPayLoad tokenPayLoad) {
        adminAuthorizationValidator.checkIsAdminOrHigher(tokenPayLoad);
        return ResponseEntity.ok(reportRegisterService.findReports());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@Auth TokenPayLoad tokenPayLoad, @PathVariable Long reportId) {
        adminAuthorizationValidator.checkIsAdminOrHigher(tokenPayLoad);
        return ResponseEntity.ok(reportRegisterService.findReport(reportId));
    }

    @PatchMapping
    public ResponseEntity<Void> updateReportStatus(@Auth TokenPayLoad tokenPayLoad, @RequestBody ReportUpdateRequest reportUpdateRequest) {
        adminAuthorizationValidator.checkIsAdminOrHigher(tokenPayLoad);
        reportRegisterService.update(reportUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
