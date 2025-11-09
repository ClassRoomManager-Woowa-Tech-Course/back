package com.classroom.manager.file.infra;

import com.classroom.manager.file.infra.dto.UploadResult;
import com.classroom.manager.file.infra.exception.FileException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader implements FileStorageService {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public UploadResult upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        String s3Key = generateS3Key(originalFilename);

        ObjectMetadata objectMetadata = ObjectMetadata.builder()
                .contentType(fileType)
                .contentLength(fileSize)
                .contentDisposition("inline")
                .build();

        S3Resource s3Resource = getS3Resource(file, s3Key, objectMetadata);

        String fileUrl = getFileUrl(s3Resource);
        return new UploadResult(fileUrl, originalFilename, fileType);
    }

    private static String getFileUrl(S3Resource s3Resource) {
        String fileUrl;
        try {
            fileUrl = s3Resource.getURL().toString();
        } catch (IOException e) {
            throw new FileException("S3 리소스 URL 조회에 실패했습니다.", e);
        }
        return fileUrl;
    }

    private S3Resource getS3Resource(MultipartFile file, String s3Key, ObjectMetadata objectMetadata) {
        S3Resource s3Resource;
        try (InputStream inputStream = file.getInputStream()){
            s3Resource = s3Template.upload(bucketName, s3Key, inputStream, objectMetadata);
        } catch (IOException e) {
            throw new FileException("S3 업로드 중 파일 스트림 오류가 발생했습니다.", e);
        }
        return s3Resource;
    }

    @Override
    public void delete(String fileKey) {
        try {
            s3Template.deleteObject(bucketName, fileKey);
        } catch (Exception e) {
            throw new FileException("S3 파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private String generateS3Key(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return UUID.randomUUID() + (extension != null ? "." + extension : "");
    }
}
