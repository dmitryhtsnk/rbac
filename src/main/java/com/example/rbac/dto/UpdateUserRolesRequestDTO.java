package com.example.rbac.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserRolesRequestDTO {
    private Set<String> roleNames;
}
