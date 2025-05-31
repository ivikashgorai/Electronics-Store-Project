package com.electronics.store.repositories;

import com.electronics.store.entities.UserRole;
import com.electronics.store.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
