package com.classroom.manager.user.domain.repository;

import com.classroom.manager.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
