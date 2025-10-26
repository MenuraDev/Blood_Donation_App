package com.blooddonation.app.config;

import com.blooddonation.app.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil, UserService userService) {
        return new JwtAuthFilter(jwtUtil, userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService, PasswordEncoder passwordEncoder) throws Exception {
        return authentication -> {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userService);
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            return authenticationProvider.authenticate(authentication);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login", "/register", "/donor-signup", "/dashboard", "/user-management",
                "/blood-request-management", "/blood-unit-management", "/event-management",
                "/donation-management", "/blood-donation-events", "/donations-history",
                "/event-register-management", "/api/auth/**", "/css/**", "/img/**", "/js/**", "/webjars/**", "/favicon.ico", "/strategy-management")
                        .permitAll()
                        .requestMatchers("/api/donations/history").hasRole("DONOR") // Require DONOR role for donation history page
                        .requestMatchers("/api/strategies/**").hasRole("IT_OFFICER") // New rule for strategy management
                        .requestMatchers("/api/donations/donor/**").hasRole("DONOR") // Allow donors to view their history
                        .requestMatchers("/api/donations/**", "/api/blood-requests/**", "/api/blood-units/**",
                                "/api/events/**", "/api/users/**", "/api/event-registers/**", "/api/donors/**")
                        .authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
