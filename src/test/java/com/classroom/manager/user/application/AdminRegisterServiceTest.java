package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminAlreadyExistException;
import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Active;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.Role;
import com.classroom.manager.user.application.dto.RegisterAdminRequest;
import com.classroom.manager.user.domain.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminRegisterServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminRegisterService adminRegisterService;

    private RegisterAdminRequest registerAdminRequest;


    @BeforeEach
    void setUp() {
        registerAdminRequest = RegisterAdminRequest.builder()
                .adminId("20200000")
                .name("admin")
                .password("admin")
                .authorization(Authorization.ADMIN)
                .contact("010-0000-0000")
                .role(Role.STUDENT)
                .build();
    }

    @DisplayName("새로운 관리자를 등록하면 AdminRepository에 저장된다.")
    @Test
    void adminRegister() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(false);
        when(passwordEncoder.encode(registerAdminRequest.password())).thenReturn("encoded");

        adminRegisterService.register(registerAdminRequest);

        verify(adminRepository).save(Mockito.any(Admin.class));
    }

    @DisplayName("이미 존재하는 관리자이면 에러를 발생한다.")
    @Test
    void adminRegisterFail() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(true);

        assertThatThrownBy(() -> adminRegisterService.register(registerAdminRequest))
                .isInstanceOf(AdminAlreadyExistException.class);
    }

    @DisplayName("AdminId가 일치하는 관리자가 있으면 삭제한다.")
    @Test
    void adminDelete() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(true);

        adminRegisterService.delete(registerAdminRequest);

        verify(adminRepository).deleteById(registerAdminRequest.adminId());
    }

    @DisplayName("AdminId가 일치하는 관리자가 없으면 에러가 발생한다.")
    @Test
    void adminDeleteFail() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(false);

        assertThatThrownBy(() -> adminRegisterService.delete(registerAdminRequest))
                .isInstanceOf(AdminNotFoundException.class);
    }

    @DisplayName("AdminId가 일치하는 관리자가 있으면 휴면 처리한다.")
    @Test
    void adminUpdate() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(true);
        Admin admin = Admin.builder()
                .adminId(registerAdminRequest.adminId())
                .active(Active.ACTIVE)
                .build();
        when(adminRepository.findById(registerAdminRequest.adminId())).thenReturn(Optional.of(admin));

        adminRegisterService.suspend(registerAdminRequest);

        verify(adminRepository).findById(registerAdminRequest.adminId());
        verify(adminRepository).save(Mockito.any(Admin.class));

        assertThat(ReflectionTestUtils.getField(admin, "active")).isEqualTo(Active.INACTIVE);
    }

    @DisplayName("AdminId가 일치하는 관리자가 없으면 에러가 발생한다.")
    @Test
    void adminUpdateFail() {
        when(adminRepository.existsById(registerAdminRequest.adminId())).thenReturn(false);

        assertThatThrownBy(() -> adminRegisterService.suspend(registerAdminRequest))
                .isInstanceOf(AdminNotFoundException.class);
    }
}
