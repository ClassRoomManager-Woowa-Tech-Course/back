package com.classroom.manager.guideline.presentation;

import com.classroom.manager.guideline.application.GuideLineService;
import com.classroom.manager.guideline.application.dto.GuideLineRegisterRequest;
import com.classroom.manager.guideline.presentation.dto.GuideLineResponse;
import com.classroom.manager.user.infra.security.annotation.Auth;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guideLines")
public class GuideLineController {

    private final GuideLineService guideLineService;

    @PostMapping
    public ResponseEntity<Void> registerGuideLine(
            @Auth TokenPayLoad tokenPayLoad,
            @RequestPart("guideLine") GuideLineRegisterRequest guideLineRegisterRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        String adminId = tokenPayLoad.adminId();
        guideLineService.register(adminId, guideLineRegisterRequest, files);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GuideLineResponse>> getGuideLines() {
        return ResponseEntity.ok(guideLineService.findGuideLines());
    }
}
