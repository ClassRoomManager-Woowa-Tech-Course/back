package com.classroom.manager.report.application.dto;

import com.classroom.manager.report.domain.Status;

public record ReportUpdateRequest(
        Long reportId,
        Status status
) {
}
