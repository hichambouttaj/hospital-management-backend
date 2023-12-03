package com.ghopital.projet.config;

import com.ghopital.projet.auth.JwtAuthEntryPoint;
import com.ghopital.projet.auth.JwtAuthFilter;
import com.ghopital.projet.dto.ROLES;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {
    public static final String SWAGGER_UI_URL = "/swagger-ui/**";
    public static final String API_DOCS_URL = "/v3/api-docs/**";
    public static final String[] ALLOWED_URLS = {
            SWAGGER_UI_URL, API_DOCS_URL
    };
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint, JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:5173"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    return configuration;
                }));

        http
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(ALLOWED_URLS).permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/auth/info").permitAll()
                            .requestMatchers("/api/v1/employees/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_EMPLOYEES.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/files/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_EMPLOYEES.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/materials/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/medications/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/locations/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/suppliers/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/stocks/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/stockOrders/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/productOrders/**").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers(HttpMethod.GET, "/api/v1/services").hasAnyAuthority(ROLES.ROLE_MANAGEMENT_PRODUCT.name(), ROLES.ROLE_MANAGEMENT_PHARMACY.name(), ROLES.ROLE_MANAGEMENT_MATERIALS.name(), ROLES.ROLE_ADMIN.name())
                            .requestMatchers("/api/v1/**").hasAuthority(ROLES.ROLE_ADMIN.name())
                            .anyRequest().authenticated();
                });

        http
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
