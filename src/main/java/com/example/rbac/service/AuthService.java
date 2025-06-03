package com.example.rbac.service;

import com.example.rbac.config.UserDetailsImpl;
import com.example.rbac.dto.JwtResponse;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.SignupRequest;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;  // Твій клас для роботи з JWT
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Логін: аутентифікація користувача та генерація JWT
    public JwtResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();


        String token = jwtTokenProvider.generateToken(authentication);

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new JwtResponse(token, user.getId(), user.getUsername(), roles);
    }

    // Реєстрація нового користувача
    public void registerUser(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            roles.add(userRole);
        } else {
            List<Role> foundRoles = roleRepository.findByNameIn(request.getRoles());
            if (foundRoles.size() != request.getRoles().size()) {
                throw new RuntimeException("One or more roles not found");
            }
            roles.addAll(foundRoles);
        }

        user.setRoles(roles);
        userRepository.save(user);
    }
}
