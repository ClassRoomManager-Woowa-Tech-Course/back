package com.classroom.manager.user.application;

import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuperAdminInitializerTest {

    private final String TEST_ID = "super_admin";
    private final String TEST_PW = "test_password123";
    private final String TEST_NAME = "Test Admin";
    private final String TEST_CONTACT = "010-1111-2222";

    @Mock
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private SuperAdminInitializer superAdminInitializer;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        superAdminInitializer = new SuperAdminInitializer(adminRepository, passwordEncoder);

        ReflectionTestUtils.setField(superAdminInitializer, "adminId", TEST_ID);
        ReflectionTestUtils.setField(superAdminInitializer, "adminPassword", TEST_PW);
        ReflectionTestUtils.setField(superAdminInitializer, "adminName", TEST_NAME);
        ReflectionTestUtils.setField(superAdminInitializer, "adminContact", TEST_CONTACT);
    }

    @DisplayName("관리자가 없을 때, 고정된 전체 관리자 계정을 생성한다 (데이터 시딩)")
    @Test
    void registerSuperAdmin_whenNotExists() throws Exception {
        when(adminRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        superAdminInitializer.run();

        verify(adminRepository, times(1)).save(any(Admin.class));

        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(adminCaptor.capture()); // 캡처

        Admin capturedAdmin = adminCaptor.getValue();
        assertThat(ReflectionTestUtils.getField(capturedAdmin, "adminId")).isEqualTo(TEST_ID);
        assertThat(ReflectionTestUtils.getField(capturedAdmin, "name")).isEqualTo(TEST_NAME);
        assertThat(ReflectionTestUtils.getField(capturedAdmin, "authorization")).isEqualTo(Authorization.SUPER_ADMIN);
        assertThat(ReflectionTestUtils.getField(capturedAdmin, "contact")).isEqualTo(TEST_CONTACT);
        String capturedPassword = (String) ReflectionTestUtils.getField(capturedAdmin, "password");
        assertThat(passwordEncoder.matches(TEST_PW, capturedPassword)).isTrue();
    }

    @DisplayName("관리자가 이미 있을 때, 계정을 생성하지 않는다 (스킵)")
    @Test
    void skipRegister_whenAlreadyExists() throws Exception {
        Admin existingAdmin = new Admin();
        when(adminRepository.findById(TEST_ID)).thenReturn(Optional.of(existingAdmin));

        superAdminInitializer.run();

        verify(adminRepository, never()).save(any(Admin.class));
    }
}
