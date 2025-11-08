package com.classroom.manager.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
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
}
