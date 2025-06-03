package com.example.rbac.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserAdminViewDTO {
    private Long id;
    private String username;
    private Set<String> roles;
}
