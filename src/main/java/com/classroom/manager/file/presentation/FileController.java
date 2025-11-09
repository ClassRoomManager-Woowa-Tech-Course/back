package com.classroom.manager.file.presentation;

import com.classroom.manager.file.infra.FileStorageService;
import com.classroom.manager.file.infra.dto.UploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        UploadResult upload = fileStorageService.upload(file);
        return upload.fileUrl();
    }
}
