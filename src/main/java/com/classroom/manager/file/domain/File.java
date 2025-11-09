package com.classroom.manager.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileRelatedType relatedType;

    private String fileUrl;
    private String fileType;
    private String originalFileName;
    private Long relatedId;

    public String fileUrl() {
        return fileUrl;
    }

    public Long id() {
        return id;
    }

    public Long relatedId() {
        return relatedId;
    }
}
