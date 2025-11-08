package com.classroom.manager.user.application.dto;

import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.Role;
import lombok.Builder;

@Builder
public record RegisterAdminRequest(
        String adminId,
        String name,
        String contact,
        String password,
        Role role,
        Authorization authorization
) {
}
