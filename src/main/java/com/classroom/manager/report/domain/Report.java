package com.classroom.manager.report.domain;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.report.application.dto.ReportRegisterRequest;
import com.classroom.manager.report.presentation.dto.ReportResponse;
import com.classroom.manager.user.domain.Member;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    private Item item;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime date;
    private String contact;
    private String content;

    public void complete() {
        this.status = Status.COMPLETED;
    }

    public static Report from(ReportRegisterRequest reportRegisterRequest, Member member, Classroom classroom) {
        return Report.builder()
                .member(member)
                .classroom(classroom)
                .date(reportRegisterRequest.date())
                .item(reportRegisterRequest.item())
                .status(reportRegisterRequest.status())
                .contact(reportRegisterRequest.contact())
                .content(reportRegisterRequest.content())
                .date(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
    }

    public ReportResponse to(List<String> fileUrls) {
        return ReportResponse.builder()
                .id(id)
                .roomCode(classroom.roomCode())
                .date(date)
                .status(status)
                .contact(contact)
                .memberId(member.memberId())
                .item(item)
                .content(content)
                .fileUrls(fileUrls)
                .build();
    }

    public Long relatedId() {
        return id;
    }
}
