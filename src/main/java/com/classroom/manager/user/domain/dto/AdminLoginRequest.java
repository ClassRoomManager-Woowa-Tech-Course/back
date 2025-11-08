package com.classroom.manager.user.domain.dto;

import lombok.Builder;

@Builder
public record AdminLoginRequest(
        String adminId,
        String password
) {
}
