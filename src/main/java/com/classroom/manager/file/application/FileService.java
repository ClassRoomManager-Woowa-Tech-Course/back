package com.classroom.manager.file.application;

import com.classroom.manager.file.domain.File;
import com.classroom.manager.file.domain.FileRelatedType;
import com.classroom.manager.file.domain.FileRepository;
import com.classroom.manager.file.infra.FileStorageService;
import com.classroom.manager.file.infra.dto.UploadResult;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    public void uploadAllFiles(Long relatedId, FileRelatedType fileRelatedType, List<MultipartFile> files) {
        files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .forEach(file -> uploadFile(relatedId, fileRelatedType, file));
    }

    private void uploadFile(Long relatedId, FileRelatedType fileRelatedType, MultipartFile multipartFile) {
        UploadResult result = fileStorageService.upload(multipartFile);
        File file = File.builder()
                .fileUrl(result.fileUrl())
                .originalFileName(result.originalFileName())
                .fileType(result.fileType())
                .relatedId(relatedId)
                .relatedType(fileRelatedType).build();
        fileRepository.save(file);
    }

    public void deleteAllFiles(Long reportId, FileRelatedType fileRelatedType) {
        List<File> files = findFilesByRelatedId(reportId, fileRelatedType);
        for (File file : files) {
            deleteFile(file.id());
        }
    }

    private void deleteFile(Long fileId) {
        File fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다: " + fileId));
        fileStorageService.delete(fileEntity.fileUrl());
        fileRepository.deleteById(fileId);
    }

    public List<File> findFilesByRelatedId(Long relatedId, FileRelatedType fileRelatedType) {
        return fileRepository.findByRelatedIdAndRelatedType(relatedId, fileRelatedType);
    }

    public List<File> findFilesByRelatedIds(List<Long> relatedIds, FileRelatedType fileRelatedType) {
        return fileRepository.findAllByRelatedIdInAndRelatedType(relatedIds, fileRelatedType);
    }
    public List<String> extractFileUrls(List<File> files) {
        return files.stream().map(File::fileUrl).toList();
    }
}
