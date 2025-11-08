package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.dto.AdminLoginRequest;
import com.classroom.manager.user.domain.repository.AdminRepository;
import com.classroom.manager.user.infra.security.JwtProvider;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String login(AdminLoginRequest adminLoginRequest) {
        Admin admin = adminRepository.findById(adminLoginRequest.adminId())
                .orElseThrow(() -> new AdminNotFoundException(adminLoginRequest.adminId()));

        TokenPayLoad tokenPayLoad = admin.login(adminLoginRequest.password(), passwordEncoder);

        return jwtProvider.createToken(tokenPayLoad);
    }
}
