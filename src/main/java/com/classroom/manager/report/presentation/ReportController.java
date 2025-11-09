package com.classroom.manager.report.presentation;

import com.classroom.manager.report.application.ReportRegisterService;
import com.classroom.manager.report.application.dto.ReportRegisterRequest;
import com.classroom.manager.report.application.dto.ReportUpdateRequest;
import com.classroom.manager.report.presentation.dto.ReportResponse;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerReport(
            @RequestPart("report") ReportRegisterRequest reportRegisterRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        reportRegisterService.register(reportRegisterRequest, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportRegisterService.findReports());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportRegisterService.findReport(reportId));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateReportStatus(@RequestBody ReportUpdateRequest reportUpdateRequest) {
        reportRegisterService.update(reportUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
