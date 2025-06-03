package com.example.rbac.service;

import com.example.rbac.model.Role;
import com.example.rbac.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    // Створити нову роль
    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    // Отримати всі ролі
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Знайти ролі за назвами (для UserService)
    public Set<Role> findRolesByNames(Set<String> names) {
        List<Role> rolesList = roleRepository.findByNameIn(names);
        return new HashSet<>(rolesList);
    }
}
