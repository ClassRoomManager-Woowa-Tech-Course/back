package com.classroom.manager.user.domain;

import com.classroom.manager.user.application.dto.AdminRegisterRequest;
import com.classroom.manager.user.domain.exception.AdminLoginFailedException;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    private String adminId;
    private String name;
    private String contact;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Authorization authorization;
    @Enumerated(EnumType.STRING)
    private Active active;

    public static Admin from(AdminRegisterRequest adminRegisterRequest, PasswordEncoder passwordEncoder) {
        return Admin.builder()
                .adminId(adminRegisterRequest.adminId())
                .role(adminRegisterRequest.role())
                .name(adminRegisterRequest.name())
                .password(passwordEncoder.encode(adminRegisterRequest.password()))
                .contact(adminRegisterRequest.contact())
                .authorization(adminRegisterRequest.authorization())
                .active(Active.ACTIVE)
                .build();
    }

    public TokenPayLoad login(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new AdminLoginFailedException();
        }
        if (this.active != Active.ACTIVE) {
            throw new AdminLoginFailedException("비활성화된 계정입니다.");
        }
        return new TokenPayLoad(adminId, authorization);
    }

    public void inactive() {
        this.active = Active.INACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin admin)) return false;
        return adminId.equals(admin.adminId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId);
    }
}
