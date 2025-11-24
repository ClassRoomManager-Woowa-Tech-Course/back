package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminAlreadyExistException;
import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Active;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.Role;
import com.classroom.manager.user.application.dto.AdminRegisterRequest;
import com.classroom.manager.user.domain.repository.AdminRepository;
import com.classroom.manager.user.domain.repository.MemberRepository;
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

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AdminRegisterService adminRegisterService;

    private AdminRegisterRequest adminRegisterRequest;


    @BeforeEach
    void setUp() {
        adminRegisterRequest = AdminRegisterRequest.builder()
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
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(false);
        when(passwordEncoder.encode(adminRegisterRequest.password())).thenReturn("encoded");

        adminRegisterService.register(adminRegisterRequest);

        verify(adminRepository).save(Mockito.any(Admin.class));
    }

    @DisplayName("이미 존재하는 관리자이면 에러를 발생한다.")
    @Test
    void adminRegisterFail() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(true);

        assertThatThrownBy(() -> adminRegisterService.register(adminRegisterRequest))
                .isInstanceOf(AdminAlreadyExistException.class);
    }

    @DisplayName("AdminId가 일치하는 관리자가 있으면 삭제한다.")
    @Test
    void adminDelete() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(true);

        adminRegisterService.delete(adminRegisterRequest);

        verify(adminRepository).deleteById(adminRegisterRequest.adminId());
    }

    @DisplayName("AdminId가 일치하는 관리자가 없으면 에러가 발생한다.")
    @Test
    void adminDeleteFail() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(false);

        assertThatThrownBy(() -> adminRegisterService.delete(adminRegisterRequest))
                .isInstanceOf(AdminNotFoundException.class);
    }

    @DisplayName("AdminId가 일치하는 관리자가 있으면 휴면 처리한다.")
    @Test
    void adminUpdate() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(true);
        Admin admin = Admin.builder()
                .adminId(adminRegisterRequest.adminId())
                .active(Active.ACTIVE)
                .build();
        when(adminRepository.findById(adminRegisterRequest.adminId())).thenReturn(Optional.of(admin));

        adminRegisterService.suspend(adminRegisterRequest);

        verify(adminRepository).findById(adminRegisterRequest.adminId());
        verify(adminRepository).save(Mockito.any(Admin.class));

        assertThat(ReflectionTestUtils.getField(admin, "active")).isEqualTo(Active.INACTIVE);
    }

    @DisplayName("AdminId가 일치하는 관리자가 없으면 에러가 발생한다.")
    @Test
    void adminUpdateFail() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(false);

        assertThatThrownBy(() -> adminRegisterService.suspend(adminRegisterRequest))
                .isInstanceOf(AdminNotFoundException.class);
    }

    @DisplayName("관리자 등록을 하면 멤버 등록도 같이된다.")
    @Test
    void memberRegister() {
        when(adminRepository.existsById(adminRegisterRequest.adminId())).thenReturn(false);
        when(memberRepository.existsById(adminRegisterRequest.adminId())).thenReturn(false);

        adminRegisterService.register(adminRegisterRequest);

        verify(memberRepository).save(Mockito.any(Member.class));
    }
}
