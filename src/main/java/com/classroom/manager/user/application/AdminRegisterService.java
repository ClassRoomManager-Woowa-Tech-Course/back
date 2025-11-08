package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminAlreadyExistException;
import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.application.dto.RegisterAdminRequest;
import com.classroom.manager.user.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminRegisterService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterAdminRequest registerAdminRequest) {
        if (adminRepository.existsById(registerAdminRequest.adminId())) {
            throw new AdminAlreadyExistException(registerAdminRequest.adminId());
        }
        Admin admin = Admin.from(registerAdminRequest, passwordEncoder);
        adminRepository.save(admin);
    }

    public void delete(RegisterAdminRequest registerAdminRequest) {
        if (adminRepository.existsById(registerAdminRequest.adminId())) {
            adminRepository.deleteById(registerAdminRequest.adminId());
            return;
        }
        throw new AdminNotFoundException(registerAdminRequest.adminId());
    }

    public void suspend(RegisterAdminRequest registerAdminRequest) {
        if (adminRepository.existsById(registerAdminRequest.adminId())) {
            Optional<Admin> admin = adminRepository.findById(registerAdminRequest.adminId());
            admin.ifPresent(a -> {
                a.inactive();
                adminRepository.save(a);
            });
            return;
        }
        throw new AdminNotFoundException(registerAdminRequest.adminId());
    }
}
