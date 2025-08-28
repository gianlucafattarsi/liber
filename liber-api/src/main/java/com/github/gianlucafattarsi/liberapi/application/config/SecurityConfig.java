package com.github.gianlucafattarsi.liberapi.application.config;

import com.github.gianlucafattarsi.liberapi.application.config.security.BearerTokenAccessDeniedHandler;
import com.github.gianlucafattarsi.liberapi.application.config.security.BearerTokenAuthenticationEntryPoint;
import com.github.gianlucafattarsi.liberapi.application.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private static final String[] permitAllWebSocketMatchers = new String[]{"/ws/**", "/topic/**"};

    private final JwtAuthFilter jwtAuthFilter;

    private final BearerTokenAccessDeniedHandler bearerTokenAccessDeniedHandler;
    private final BearerTokenAuthenticationEntryPoint bearerTokenAuthenticationEntryPoint;

    // Constructor injection for required dependencies
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          BearerTokenAccessDeniedHandler bearerTokenAccessDeniedHandler,
                          BearerTokenAuthenticationEntryPoint bearerTokenAuthenticationEntryPoint
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.bearerTokenAccessDeniedHandler = bearerTokenAccessDeniedHandler;
        this.bearerTokenAuthenticationEntryPoint = bearerTokenAuthenticationEntryPoint;
    }

    /*
     * Main security configuration
     * Defines endpoint access rules and JWT filter setup
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/openapi.json/**",
                                "/swagger-ui/**")
                        .permitAll()

                        .requestMatchers(permitAllWebSocketMatchers)
                        .permitAll()

                        // User registration endpoint
                        .requestMatchers(HttpMethod.POST, "/api/users")
                        .permitAll()

                        // Role-based endpoints
                        .requestMatchers("/api/settings/**")
                        .hasAuthority("USER_ADMIN")

                        // All other endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )

                // Stateless session (required for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2ResourceServer(server -> server
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(bearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(bearerTokenAccessDeniedHandler)
                )

                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * Password encoder bean (uses BCrypt hashing)
     * Critical for secure password storage
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}