package com.classroom.manager.user.infra.security;

import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthorizationValidator {

    public void checkIsAdminOrHigher(TokenPayLoad payload) {
        if (!payload.authorization().equals(Authorization.ADMIN) &&
                !payload.authorization().equals(Authorization.SUPER_ADMIN)) {
            throw new AccessDeniedException("접근 권한이 없습니다. (ADMIN 이상 필요)");
        }
    }

    public void checkIsSuperAdmin(TokenPayLoad payload) {
        if (!payload.authorization().equals(Authorization.SUPER_ADMIN)) {
            throw new AccessDeniedException("전체 관리자(SUPER_ADMIN) 권한이 필요합니다.");
        }
    }

    public void checkCanRegister(TokenPayLoad payLoad, Authorization requestedAuth) {
        checkIsAdminOrHigher(payLoad);
        if (payLoad.authorization().equals(Authorization.SUPER_ADMIN)) {
            return;
        }
        if (requestedAuth == Authorization.SUPER_ADMIN) {
            throw new AccessDeniedException("ADMIN 권한으로는 SUPER_ADMIN 계정을 생성할 수 없습니다.");
        }
    }
}
