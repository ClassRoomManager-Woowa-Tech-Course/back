package com.classroom.manager.report.application.dto;

import com.classroom.manager.report.domain.Item;
import com.classroom.manager.report.domain.Status;
import com.classroom.manager.user.domain.Role;

import java.time.LocalDateTime;

public record ReportRegisterRequest(
        Item item,
        Role role,
        LocalDateTime date,
        String memberId,
        String roomCode,
        String contact,
        String content,
        Status status
) {
}
