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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GuideLineService {

    private final GuideLineRepository guideLineRepository;
    private final AdminRepository adminRepository;
    private final ClassroomRepository classroomRepository;
    private final FileService fileService;

    public void register(String adminId, GuideLineRegisterRequest guideLineRegisterRequest, List<MultipartFile> files) {
        Admin admin = adminRepository.getByAdminId(adminId);
        Classroom classroom = classroomRepository.getByRoomCode(guideLineRegisterRequest.roomCode());
        GuideLine guideLine = guideLineRepository.save(GuideLine.from(admin, classroom, guideLineRegisterRequest));
        fileService.uploadAllFiles(guideLine.relatedId(), FileRelatedType.GUIDELINE, files);
    }

    @Transactional
    public List<GuideLineResponse> findGuideLines() {
        List<GuideLine> guideLines = guideLineRepository.findAll();
        List<Long> guideLineIds = guideLines.stream().map(GuideLine::relatedId).toList();
        List<File> allFiles = fileService.findFilesByRelatedIds(guideLineIds, FileRelatedType.GUIDELINE);
        Map<Long, List<File>> filesMap = allFiles.stream()
                .collect(Collectors.groupingBy(File::relatedId));
        return guideLines.stream()
                .map(guideLine -> {
                    List<File> files = filesMap.getOrDefault(guideLine.relatedId(), List.of());
                    return guideLine.to(fileService.extractFileUrls(files));
                }).toList();
    }

    @Transactional
    public GuideLineResponse findGuideLine(Long guideLineId) {
        List<File> files = fileService.findFilesByRelatedId(guideLineId, FileRelatedType.GUIDELINE);
        List<String> urls = fileService.extractFileUrls(files);
        return guideLineRepository.getGuideLineById(guideLineId).to(urls);
    }
}
