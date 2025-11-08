package com.classroom.manager.user.domain;

import com.classroom.manager.user.domain.dto.RegisterAdminRequest;
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

    public static Admin from(RegisterAdminRequest registerAdminRequest, PasswordEncoder passwordEncoder) {
        return Admin.builder()
                .adminId(registerAdminRequest.adminId())
                .role(registerAdminRequest.role())
                .name(registerAdminRequest.name())
                .password(passwordEncoder.encode(registerAdminRequest.password()))
                .contact(registerAdminRequest.contact())
                .authorization(registerAdminRequest.authorization())
                .active(Active.ACTIVE)
                .build();
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
