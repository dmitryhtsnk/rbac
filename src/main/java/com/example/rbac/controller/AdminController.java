package com.example.rbac.controller;

import com.example.rbac.dto.UserAdminViewDTO;
import com.example.rbac.dto.UpdateUserRolesRequestDTO;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<UserAdminViewDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> new UserAdminViewDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles().stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @PutMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public UserAdminViewDTO updateUserRoles(
            @PathVariable Long userId,
            @RequestBody UpdateUserRolesRequestDTO request
    ) {
        User user = userService.updateUserRoles(userId, request.getRoleNames());
        return new UserAdminViewDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
