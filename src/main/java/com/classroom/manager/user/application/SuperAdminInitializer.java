package com.classroom.manager.user.application;

import com.classroom.manager.user.domain.Active;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.Role;
import com.classroom.manager.user.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.super.id}")
    private String adminId;

    @Value("${admin.super.password}")
    private String adminPassword;

    @Value("${admin.super.name}")
    private String adminName;

    @Value("${admin.super.contact}")
    private String adminContact;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.findById(adminId).isEmpty()) {
            Admin admin = Admin.builder()
                    .adminId(adminId)
                    .password(passwordEncoder.encode(adminPassword))
                    .name(adminName)
                    .contact(adminContact)
                    .active(Active.ACTIVE)
                    .role(Role.STAFF)
                    .authorization(Authorization.SUPER_ADMIN)
                    .build();

            adminRepository.save(admin);
            log.info("Super Admin(ID: {}) account has been initialized.", adminId);
            return;
        }
        log.info("Super Admin(ID: {}) account already exists. Skipping initialization.", adminId);
    }
}
