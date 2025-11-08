package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminAlreadyExistException;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.dto.RegisterAdminRequest;
import com.classroom.manager.user.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRegisterService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin register(RegisterAdminRequest registerAdminRequest) {
        if (adminRepository.existsById(registerAdminRequest.adminId())) {
            throw new AdminAlreadyExistException(registerAdminRequest.adminId());
        }
        Admin admin = Admin.from(registerAdminRequest, passwordEncoder);
        return adminRepository.save(admin);
    }
}
