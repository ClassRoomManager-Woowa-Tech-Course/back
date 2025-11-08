package com.classroom.manager.user.application;

import com.classroom.manager.user.application.exception.AdminNotFoundException;
import com.classroom.manager.user.domain.Active;
import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.domain.dto.AdminLoginRequest;
import com.classroom.manager.user.domain.exception.AdminLoginFailedException;
import com.classroom.manager.user.domain.repository.AdminRepository;
import com.classroom.manager.user.infra.security.JwtProvider;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminLoginServiceTest {

    private final String ADMIN_ID = "adminId";
    private final String RAW_PASSWORD = "admin"; // 사용자가 입력한 비번
    private final String HASHED_PASSWORD = "hashed_admin_password"; // DB에 저장된 해시된 비번
    private final String FAKE_TOKEN = "fake.jwt.token.string"; // J

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AdminLoginService adminLoginService;

    private AdminLoginRequest adminLoginRequest;
    private Admin adminFromDb;
    private TokenPayLoad tokenPayLoad;

    @BeforeEach
    void setUp() {
        adminLoginRequest = AdminLoginRequest.builder()
                .adminId(ADMIN_ID)
                .password(RAW_PASSWORD)
                .build();
        adminFromDb = Admin.builder()
                .adminId(ADMIN_ID)
                .password(HASHED_PASSWORD)
                .authorization(Authorization.ADMIN)
                .active(Active.ACTIVE)
                .build();
        tokenPayLoad = new TokenPayLoad(ADMIN_ID, Authorization.ADMIN);
    }

    @DisplayName("등록된 관리자가 로그인 하면 Token을 발급한다.")
    @Test
    void adminLogin() {
        when(adminRepository.findById(ADMIN_ID)).thenReturn(Optional.ofNullable(adminFromDb));
        when(passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtProvider.createToken(tokenPayLoad)).thenReturn(FAKE_TOKEN);

        String token = adminLoginService.login(adminLoginRequest);

        assertThat(token).isEqualTo(FAKE_TOKEN);
    }

    @DisplayName("등록되지 않은 관리자가 로그인 하면 에러가 발생한다.")
    @Test
    void adminLoginFail() {
        when(adminRepository.findById(ADMIN_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminLoginService.login(adminLoginRequest))
                .isInstanceOf(AdminNotFoundException.class);

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtProvider, never()).createToken(any(TokenPayLoad.class));
    }

    @DisplayName("비밀번호가 틀리면 에러가 발생한다.")
    @Test
    void passwordMismatch() {
        when(adminRepository.findById(ADMIN_ID)).thenReturn(Optional.ofNullable(adminFromDb));
        when(passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertThatThrownBy(() -> adminLoginService.login(adminLoginRequest))
                .isInstanceOf(AdminLoginFailedException.class);

        verify(jwtProvider, never()).createToken(any(TokenPayLoad.class));
    }
}
