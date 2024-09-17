package com.wap.wabi.auth.admin.repository;

import com.wap.wabi.auth.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("select a from Admin a where a.id = :id and a.password = :password")
    Optional<Admin> findAdminByIdAndPassword(@Param("id") String id, @Param("password") String password);
}
