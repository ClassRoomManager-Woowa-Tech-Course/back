package com.classroom.manager.guideline.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GuideLineResponse(
        Long guideLineId,
        LocalDateTime date,
        String roomCode,
        String content,
        List<String> fileUrls
) {
}
