package com.classroom.manager.report.presentation.dto;

import com.classroom.manager.report.domain.Item;
import com.classroom.manager.report.domain.Status;
import java.util.List;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponse (
        Long id,
        String memberId,
        String contact,
        String roomCode,
        LocalDateTime date,
        Status status,
        String content,
        Item item,
        List<String> fileUrls
) {
}
