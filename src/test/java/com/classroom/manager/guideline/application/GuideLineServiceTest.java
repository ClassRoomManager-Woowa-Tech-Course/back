package com.classroom.manager.guideline.application;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import com.classroom.manager.file.application.FileService;
import com.classroom.manager.file.domain.File;
import com.classroom.manager.file.domain.FileRelatedType;
import com.classroom.manager.guideline.application.dto.GuideLineRegisterRequest;
import com.classroom.manager.guideline.domain.GuideLine;
import com.classroom.manager.guideline.domain.repository.GuideLineRepository;
import com.classroom.manager.guideline.presentation.dto.GuideLineResponse;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.repository.AdminRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuideLineServiceTest {

    private GuideLineRepository guideLineRepository;
    private AdminRepository adminRepository;
    private ClassroomRepository classroomRepository;
    private FileService fileService;
    private GuideLineService guideLineService;

    @BeforeEach
    void setUp() {
        guideLineRepository = mock(GuideLineRepository.class);
        adminRepository = mock(AdminRepository.class);
        classroomRepository = mock(ClassroomRepository.class);
        fileService = mock(FileService.class);

        guideLineService = new GuideLineService(
                guideLineRepository,
                adminRepository,
                classroomRepository,
                fileService
        );
    }

    @DisplayName("가이드라인을 저장하면 파일도 같이 저장된다.")
    @Test
    void registerShouldSaveGuideLineAndUploadFiles() {
        String adminId = "super_admin";
        GuideLineRegisterRequest request = new GuideLineRegisterRequest("627-A", "content");

        Admin admin = mock(Admin.class);
        Classroom classroom = mock(Classroom.class);

        GuideLine savedGuideLine = mock(GuideLine.class);

        when(adminRepository.getByAdminId(adminId)).thenReturn(admin);
        when(classroomRepository.getByRoomCode("627-A")).thenReturn(classroom);
        when(guideLineRepository.save(any(GuideLine.class))).thenReturn(savedGuideLine);
        when(savedGuideLine.relatedId()).thenReturn(100L);

        List<MultipartFile> files = List.of(mock(MultipartFile.class), mock(MultipartFile.class));


        guideLineService.register(adminId, request, files);

        ArgumentCaptor<GuideLine> guideLineCaptor = ArgumentCaptor.forClass(GuideLine.class);
        verify(guideLineRepository, times(1)).save(guideLineCaptor.capture());
        verify(fileService, times(1))
                .uploadAllFiles(eq(100L), eq(FileRelatedType.GUIDELINE), eq(files));
    }

    @DisplayName("가이드라인을 조회하면 파일 URL도 같이 반환한다.")
    @Test
    void findGuideLinesShouldReturnResponsesWithFileUrls() {
        GuideLine guide1 = mock(GuideLine.class);
        GuideLine guide2 = mock(GuideLine.class);

        when(guide1.relatedId()).thenReturn(1L);
        when(guide2.relatedId()).thenReturn(2L);

        when(guideLineRepository.findAll()).thenReturn(List.of(guide1, guide2));

        File file1 = mock(File.class);
        File file2 = mock(File.class);

        when(file1.relatedId()).thenReturn(1L);
        when(file2.relatedId()).thenReturn(2L);
        when(fileService.findFilesByRelatedIds(List.of(1L, 2L), FileRelatedType.GUIDELINE))
                .thenReturn(List.of(file1, file2));

        when(fileService.extractFileUrls(List.of(file1)))
                .thenReturn(List.of("url1"));
        when(fileService.extractFileUrls(List.of(file2)))
                .thenReturn(List.of("url2"));

        GuideLineResponse response1 = mock(GuideLineResponse.class);
        GuideLineResponse response2 = mock(GuideLineResponse.class);

        when(guide1.to(List.of("url1"))).thenReturn(response1);
        when(guide2.to(List.of("url2"))).thenReturn(response2);

        List<GuideLineResponse> responses = guideLineService.findGuideLines();

        assertThat(responses).hasSize(2);
        assertThat(responses).containsExactly(response1, response2);
    }
}
