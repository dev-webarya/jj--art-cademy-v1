package com.artacademy.config;

import com.artacademy.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutHandler logoutHandler;

        private static final String[] SWAGGER_WHITELIST = {
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(withDefaults())
                                .authorizeHttpRequests(auth -> auth
                                                // Infrastructure-level public routes (must be permitted at filter
                                                // level)
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()

                                                // Payment Webhooks (Must be public as they come from External Gateways
                                                // like Razorpay)
                                                .requestMatchers("/api/v1/payments/webhook/**").permitAll()

                                                // Public catalog browsing endpoints (GET only)
                                                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/collections/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/product-images/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/reviews/product/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/stores/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/attribute-types/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/attribute-values/**")
                                                .permitAll()

                                                // Public Art Academy Endpoints (GET only)
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-works/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-works-categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-classes/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-classes-categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-exhibitions/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/v1/art-exhibitions-categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-galleries/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-galleries-categories/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-materials/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/art-materials-categories/**")
                                                .permitAll()

                                                // All other authorization is handled by @PreAuthorize annotations in
                                                // controllers
                                                // This provides better flexibility, maintainability, and co-location of
                                                // security rules
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout
                                                .logoutUrl("/api/v1/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                        response.setContentType("application/json");
                                                        response.getWriter()
                                                                        .write("{\"message\": \"Logout successful.\"}");
                                                }))
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(List.of("*")); // Allow all origins
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}