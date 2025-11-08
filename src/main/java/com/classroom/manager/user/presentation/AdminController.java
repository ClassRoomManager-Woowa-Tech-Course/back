package com.classroom.manager.user.presentation;

import com.classroom.manager.user.application.AdminLoginService;
import com.classroom.manager.user.application.AdminRegisterService;
import com.classroom.manager.user.application.dto.AdminLoginRequest;
import com.classroom.manager.user.application.dto.RegisterAdminRequest;
import com.classroom.manager.user.presentation.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminRegisterService adminRegisterService;
    private final AdminLoginService adminLoginService;

    @PostMapping
    public ResponseEntity<Void> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        adminRegisterService.register(registerAdminRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        adminRegisterService.delete(registerAdminRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        adminRegisterService.suspend(registerAdminRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminLoginRequest adminLoginRequest) {
        String token = adminLoginService.login(adminLoginRequest);
        LoginResponse loginResponse = new LoginResponse(token);
        return ResponseEntity.ok(loginResponse);
    }
}
