package com.example.rbac.config;

import com.example.rbac.service.JwtAuthFilter;
import com.example.rbac.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Алгоритм хешування паролів BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Витягуємо AuthenticationManager із конфігурації Spring Security
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Вимикаємо CSRF (потрібно для stateless JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless сесії (без збереження)
                .authorizeHttpRequests(auth -> auth
                        // Дозволяємо вільний доступ до цих URL
                        .requestMatchers("/api/auth/**", "/error").permitAll()

                        // Доступ користувачам з ролями USER або ADMIN до GET /api/admin/users
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("USER", "ADMIN")

                        // Адмінські шляхи доступні лише ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Для GET-запитів до /events/** і /venues/** - USER або ADMIN
                        .requestMatchers(HttpMethod.GET, "/events/**", "/venues/**").hasAnyRole("USER", "ADMIN")

                        // Для POST, PUT, DELETE на /events/** і /venues/** - тільки ADMIN
                        .requestMatchers(HttpMethod.POST, "/events/**", "/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/events/**", "/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/events/**", "/venues/**").hasRole("ADMIN")

                        // Всі інші запити вимагають аутентифікації
                        .anyRequest().authenticated()
                )
                // Встановлюємо кастомний провайдер аутентифікації
                .authenticationProvider(authenticationProvider())

                // Додаємо JWT фільтр перед стандартним UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
