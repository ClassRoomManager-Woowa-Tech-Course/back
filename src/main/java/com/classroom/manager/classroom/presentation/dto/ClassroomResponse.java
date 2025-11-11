package com.classroom.manager.classroom.presentation.dto;

import lombok.Builder;

@Builder
public record ClassroomResponse(
        String roomCode
) {
}
