package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminAlreadyExistException;
import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.application.dto.AdminRegisterRequest;
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

    public void register(AdminRegisterRequest adminRegisterRequest) {
        if (adminRepository.existsById(adminRegisterRequest.adminId())) {
            throw new AdminAlreadyExistException(adminRegisterRequest.adminId());
        }
        Admin admin = Admin.from(adminRegisterRequest, passwordEncoder);
        adminRepository.save(admin);
    }

    public void delete(AdminRegisterRequest adminRegisterRequest) {
        if (adminRepository.existsById(adminRegisterRequest.adminId())) {
            adminRepository.deleteById(adminRegisterRequest.adminId());
            return;
        }
        throw new AdminNotFoundException(adminRegisterRequest.adminId());
    }

    public void suspend(AdminRegisterRequest adminRegisterRequest) {
        if (adminRepository.existsById(adminRegisterRequest.adminId())) {
            Optional<Admin> admin = adminRepository.findById(adminRegisterRequest.adminId());
            admin.ifPresent(a -> {
                a.inactive();
                adminRepository.save(a);
            });
            return;
        }
        throw new AdminNotFoundException(adminRegisterRequest.adminId());
    }
}
