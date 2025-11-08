package com.classroom.manager.user.infra.security.dto;

import com.classroom.manager.user.domain.Authorization;

public record TokenPayLoad(
        String adminId,
        Authorization authorization
) {
}
