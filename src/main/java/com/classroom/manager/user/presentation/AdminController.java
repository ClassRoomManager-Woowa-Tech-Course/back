package com.classroom.manager.user.presentation;

import com.classroom.manager.user.application.AdminRegisterService;
import com.classroom.manager.user.domain.dto.RegisterAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminRegisterService adminRegisterService;

    @PostMapping
    public ResponseEntity<Void> registerAdmin(RegisterAdminRequest registerAdminRequest) {
        adminRegisterService.register(registerAdminRequest);
        return ResponseEntity.ok().build();
    }
}
