package com.classroom.manager.file.infra;

import com.classroom.manager.file.infra.dto.UploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    UploadResult upload(MultipartFile file);
    void delete(String fileKey);
}
