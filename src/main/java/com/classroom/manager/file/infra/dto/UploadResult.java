package com.classroom.manager.file.infra.dto;

public record UploadResult(
    String fileUrl,
    String originalFileName,
    String fileType
) {}