package com.classroom.manager.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    private String memberId;
    private String contact;
    private Role role;
    private String name;
}
