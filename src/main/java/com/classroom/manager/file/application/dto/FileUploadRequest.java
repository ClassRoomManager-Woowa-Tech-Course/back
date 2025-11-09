package com.classroom.manager.file.application.dto;

import com.classroom.manager.file.domain.FileRelatedType;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        Long relatedId,
        FileRelatedType relatedType,
        List<MultipartFile> files
) {
}
