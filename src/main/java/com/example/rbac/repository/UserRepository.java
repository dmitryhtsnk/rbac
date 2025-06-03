package com.example.rbac.repository;

import com.example.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Метод для пошуку користувача за ім'ям (username)
    Optional<User> findByUsername(String username);

    // Додатковий метод: перевірка наявності користувача
    boolean existsByUsername(String username);
}