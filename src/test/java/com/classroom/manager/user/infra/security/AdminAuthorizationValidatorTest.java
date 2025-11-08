package com.classroom.manager.user.infra.security;

import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

class AdminAuthorizationValidatorTest {

    private AdminAuthorizationValidator validator;
    private TokenPayLoad superAdminPayload;
    private TokenPayLoad adminPayload;
    private TokenPayLoad editorPayload;

    @BeforeEach
    void setUp() {
        validator = new AdminAuthorizationValidator();
        superAdminPayload = new TokenPayLoad("super", Authorization.SUPER_ADMIN);
        adminPayload = new TokenPayLoad("admin", Authorization.ADMIN);
        editorPayload = new TokenPayLoad("editor", Authorization.EDITOR);
    }

    @DisplayName("SUPER_ADMIN은 SUPER_ADMIN을 생성할 수 있다")
    @Test
    void checkCanRegisterSuperAdminCanCreateSuperAdmin() {
        assertDoesNotThrow(() -> {
            validator.checkCanRegister(superAdminPayload, Authorization.SUPER_ADMIN);
        });
    }

    @DisplayName("SUPER_ADMIN은 ADMIN을 생성할 수 있다")
    @Test
    void checkCanRegisterSuperAdminCanCreateAdmin() {
        assertDoesNotThrow(() -> {
            validator.checkCanRegister(superAdminPayload, Authorization.ADMIN);
        });
    }

    @DisplayName("SUPER_ADMIN은 EDITER을 생성할 수 있다")
    @Test
    void checkCanRegisterSuperAdminCanCreateEditor() {
        assertDoesNotThrow(() -> {
            validator.checkCanRegister(superAdminPayload, Authorization.EDITOR);
        });
    }

    @DisplayName("ADMIN은 SUPER_ADMIN을 생성할 수 없다")
    @Test
    void checkCanRegisterAdminCannotCreateSuperAdmin() {
        assertThrows(AccessDeniedException.class, () -> {
            validator.checkCanRegister(adminPayload, Authorization.SUPER_ADMIN);
        });
    }

    @DisplayName("ADMIN은 ADMIN을 생성할 수 있다")
    @Test
    void checkCanRegisterAdminCanCreateAdmin() {
        assertDoesNotThrow(() -> {
            validator.checkCanRegister(adminPayload, Authorization.ADMIN);
        });
    }

    @DisplayName("ADMIN은 EDITOR를 생성할 수 있다")
    @Test
    void checkCanRegisterAdminCanCreateEditor() {
        assertDoesNotThrow(() -> {
            validator.checkCanRegister(adminPayload, Authorization.EDITOR);
        });
    }

    @DisplayName("EDITOR는 ADMIN을 생성할 수 없다")
    @Test
    void checkCanRegisterEditorCannotCreateAdmin() {
        assertThrows(AccessDeniedException.class, () -> {
            validator.checkCanRegister(editorPayload, Authorization.ADMIN);
        });
    }

    @DisplayName("SUPER_ADMIN은 통과한다")
    @Test
    void checkIsAdminOrHigherSuperAdmin() {
        assertDoesNotThrow(() -> validator.checkIsAdminOrHigher(superAdminPayload));
    }

    @DisplayName("ADMIN은 통과한다")
    @Test
    void checkIsAdminOrHigherAdmin() {
        assertDoesNotThrow(() -> validator.checkIsAdminOrHigher(adminPayload));
    }

    @DisplayName("EDITOR는 실패한다")
    @Test
    void checkIsAdminOrHigherEditor() {
        assertThrows(AccessDeniedException.class, () -> {
            validator.checkIsAdminOrHigher(editorPayload);
        });
    }
}