package com.classroom.manager.user.application.dto;

import lombok.Builder;

@Builder
public record AdminLoginRequest(
        String adminId,
        String password
) {
}
