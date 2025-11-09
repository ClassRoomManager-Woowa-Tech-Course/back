package com.classroom.manager.guideline.domain;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.guideline.application.dto.GuideLineRegisterRequest;
import com.classroom.manager.guideline.presentation.dto.GuideLineResponse;
import com.classroom.manager.user.domain.Admin;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuideLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;
    private String content;
    private LocalDateTime date;

    public static GuideLine from(Admin admin, Classroom classroom, GuideLineRegisterRequest guideLineRegisterRequest) {
        return GuideLine.builder()
                .date(LocalDateTime.now())
                .content(guideLineRegisterRequest.content())
                .admin(admin)
                .classroom(classroom)
                .build();
    }
    public GuideLineResponse to(List<String> fileUrls) {
        return GuideLineResponse.builder()
                .guideLineId(id)
                .date(date)
                .roomCode(classroom.roomCode())
                .content(content)
                .fileUrls(fileUrls)
                .build();
    }

    public Long relatedId() {
        return id;
    }
}
