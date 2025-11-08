package com.classroom.manager.user.presentation;

import com.classroom.manager.user.application.AdminRegisterService;
import com.classroom.manager.user.domain.dto.RegisterAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminRegisterService adminRegisterService;

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
}
