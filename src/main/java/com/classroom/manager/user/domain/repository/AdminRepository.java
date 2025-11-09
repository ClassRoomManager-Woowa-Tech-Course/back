package com.classroom.manager.user.domain.repository;

import com.classroom.manager.user.domain.Admin;
import com.classroom.manager.user.domain.exception.NotFoundAdminException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    default Admin getByAdminId(String adminId) {
        return findById(adminId).orElseThrow(NotFoundAdminException::new);
    }
}
