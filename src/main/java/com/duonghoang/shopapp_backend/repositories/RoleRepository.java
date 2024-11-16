package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
