package com.example.rbac.repository;

import com.example.rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Пошук ролі за назвою (наприклад, "ADMIN")
    Optional<Role> findByName(String name);

    // Пошук ролей за списком назв (для оновлення ролей користувача)
    List<Role> findByNameIn(Collection<String> names);
}