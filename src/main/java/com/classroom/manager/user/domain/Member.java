package com.classroom.manager.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    private String memberId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String contact;
    private String name;
}
