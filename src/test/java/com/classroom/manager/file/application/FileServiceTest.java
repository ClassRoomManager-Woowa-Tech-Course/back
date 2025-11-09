package com.classroom.manager.file.application;

import com.classroom.manager.file.domain.File;
import com.classroom.manager.file.domain.FileRelatedType;
import com.classroom.manager.file.domain.FileRepository;
import com.classroom.manager.file.infra.FileStorageService;
import com.classroom.manager.file.infra.dto.UploadResult;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    @DisplayName("파일 목록(2개)을 받아 S3 업로드와 DB 저장을 2번 실행한다")
    void uploadAllFilesSuccess() {
        Long testRelatedId = 42L;
        FileRelatedType testType = FileRelatedType.REPORT;

        MultipartFile file1 = new MockMultipartFile(
                "file1", "image.jpg", "image/jpeg", "image-bytes".getBytes()
        );
        MultipartFile file2 = new MockMultipartFile(
                "file2", "document.pdf", "application/pdf", "pdf-bytes".getBytes()
        );
        List<MultipartFile> files = List.of(file1, file2);

        UploadResult result1 = new UploadResult("s3-url-1", "image.jpg", "image/jpeg");
        UploadResult result2 = new UploadResult("s3-url-2", "document.pdf", "application/pdf");

        when(fileStorageService.upload(file1)).thenReturn(result1);
        when(fileStorageService.upload(file2)).thenReturn(result2);

        fileService.uploadAllFiles(testRelatedId, testType, files);
        verify(fileStorageService, times(2)).upload(any(MultipartFile.class));

        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileRepository, times(2)).save(fileCaptor.capture());
    }

    @Test
    @DisplayName("파일 목록에 null이나 empty 파일이 있으면 무시하고 업로드한다")
    void uploadAllFilesShouldSkipEmptyFiles() {
        Long testRelatedId = 42L;
        FileRelatedType testType = FileRelatedType.REPORT;

        MultipartFile file1 = new MockMultipartFile("file1", "image.jpg", "image/jpeg", "image".getBytes());
        MultipartFile file2_empty = new MockMultipartFile("file2", "empty.txt", "text/plain", new byte[0]);

        List<MultipartFile> files = new ArrayList<>();
                files.add(file1);
                files.add(null);
                files.add(file2_empty);

        UploadResult result1 = new UploadResult("s3-url-1", "image.jpg", "image/jpeg");
        when(fileStorageService.upload(file1)).thenReturn(result1);

        fileService.uploadAllFiles(testRelatedId, testType, files);

        verify(fileStorageService, times(1)).upload(any(MultipartFile.class));
        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test
    @DisplayName("파일 엔티티 목록(2개)을 받아 S3 삭제와 DB 삭제를 2번 실행한다")
    void deleteAllFilesSuccess() {
        File file1 = File.builder().id(101L).fileUrl("s3-key-1").build();
        File file2 = File.builder().id(102L).fileUrl("s3-key-2").build();
        List<File> files = List.of(file1, file2);

        when(fileRepository.findById(101L)).thenReturn(Optional.of(file1));
        when(fileRepository.findById(102L)).thenReturn(Optional.of(file2));

        fileService.deleteAllFiles(files);

        verify(fileStorageService, times(1)).delete("s3-key-1");
        verify(fileStorageService, times(1)).delete("s3-key-2");

        verify(fileRepository, times(1)).deleteById(101L);
        verify(fileRepository, times(1)).deleteById(102L);
    }

    @Test
    @DisplayName("삭제할 파일을 DB에서 찾지 못하면 EntityNotFoundException을 던진다")
    void deleteFileThrowsExceptionWhenFileNotFound() {
        File file1 = File.builder().id(101L).fileUrl("s3-key-1").build();

        when(fileRepository.findById(101L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.deleteAllFiles(List.of(file1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("파일을 찾을 수 없습니다: 101");

        verify(fileStorageService, never()).delete(any());
        verify(fileRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("ID와 Type으로 파일 목록을 조회하면 Repository를 호출하고 결과를 반환한다")
    void findFilesByReportIdAndRelatedTypeSuccess() {
        Long testRelatedId = 42L;
        FileRelatedType testType = FileRelatedType.REPORT;
        List<File> expectedList = List.of(new File(), new File());

        when(fileRepository.findByRelatedIdAndRelatedType(testRelatedId, testType))
                .thenReturn(expectedList);

        List<File> actualList = fileService.findFilesByReportIdAndRelatedType(testRelatedId, testType);

        verify(fileRepository, times(1)).findByRelatedIdAndRelatedType(testRelatedId, testType);
        assertThat(actualList).isEqualTo(expectedList);
    }
}
