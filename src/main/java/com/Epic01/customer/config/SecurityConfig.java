package com.Epic01.customer.config;

import com.Epic01.customer.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // ✅ Thêm annotation này để Spring nhận biết là class cấu hình
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // ✅ Rất quan trọng để Spring Boot quét được class này
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ⚠️ Cần thêm dòng này
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",            // Bao trùm cả /login và /register
                                "/api/auth/register",
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll() // ✅ Cho phép truy cập mà không cần xác thực
                        .anyRequest().authenticated() // Các request khác phải xác thực
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

}

