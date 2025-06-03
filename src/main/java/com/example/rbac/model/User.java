package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users") // Уникаємо конфлікту з SQL-зарезервованим словом "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // назва таблиці-зв’язки
            joinColumns = @JoinColumn(name = "user_id"), // посилання на User
            inverseJoinColumns = @JoinColumn(name = "role_id") // посилання на Role
    )
    private Set<Role> roles = new HashSet<>();
}
