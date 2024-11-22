package com.example.spring_auth.repositories;

import com.example.spring_auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
