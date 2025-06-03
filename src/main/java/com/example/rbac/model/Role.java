package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor          // Генерує конструктор без параметрів
@AllArgsConstructor         // Генерує конструктор з усіма полями
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Role(String name) {
        this.name = name;
    }

    @Column(unique = true, nullable = false)
    private String name;
}
