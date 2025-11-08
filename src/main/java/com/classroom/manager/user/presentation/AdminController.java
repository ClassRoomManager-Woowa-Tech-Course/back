package com.classroom.manager.user.presentation;

import com.classroom.manager.user.application.AdminLoginService;
import com.classroom.manager.user.application.AdminRegisterService;
import com.classroom.manager.user.application.dto.AdminLoginRequest;
import com.classroom.manager.user.application.dto.AdminRegisterRequest;
import com.classroom.manager.user.infra.security.AdminAuthorizationValidator;
import com.classroom.manager.user.infra.security.annotation.Auth;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import com.classroom.manager.user.presentation.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminRegisterService adminRegisterService;
    private final AdminLoginService adminLoginService;
    private final AdminAuthorizationValidator adminAuthorizationValidator;

    @PostMapping
    public ResponseEntity<Void> registerAdmin(@Auth TokenPayLoad tokenPayLoad, @RequestBody AdminRegisterRequest adminRegisterRequest) {
        adminAuthorizationValidator.checkCanRegister(tokenPayLoad, adminRegisterRequest.authorization());
        adminRegisterService.register(adminRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAdmin(@Auth TokenPayLoad tokenPayLoad, @RequestBody AdminRegisterRequest adminRegisterRequest) {
        adminAuthorizationValidator.checkIsAdminOrHigher(tokenPayLoad);
        adminRegisterService.delete(adminRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateAdmin(@Auth TokenPayLoad tokenPayLoad, @RequestBody AdminRegisterRequest adminRegisterRequest) {
        adminAuthorizationValidator.checkIsSuperAdmin(tokenPayLoad);
        adminRegisterService.suspend(adminRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminLoginRequest adminLoginRequest) {
        String token = adminLoginService.login(adminLoginRequest);
        LoginResponse loginResponse = new LoginResponse(token);
        return ResponseEntity.ok(loginResponse);
    }
}
