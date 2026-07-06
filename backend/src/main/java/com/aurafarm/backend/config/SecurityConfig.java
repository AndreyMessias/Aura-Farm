package com.aurafarm.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Dev: frontend estático servido em qualquer porta local (Live Server, http.server, etc.)
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/aurafarm/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/aurafarm/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/aurafarm/usuarios/me").authenticated()
                .requestMatchers("/aurafarm/usuarios/**").hasRole("GERENTE")
                // Fornecedor (RF001-004): exclusivo do Gerente
                .requestMatchers("/aurafarm/fornecedores/**").hasRole("GERENTE")
                // Produto (RF019-022): consulta liberada para Gerente e Funcionário; escrita exclusiva do Gerente
                .requestMatchers(HttpMethod.GET, "/aurafarm/produtos/**").authenticated()
                .requestMatchers("/aurafarm/produtos/**").hasRole("GERENTE")
                // Venda (RF013-017): Gerente e Funcionário podem cadastrar/consultar/alterar; exclusão é exclusiva do Gerente
                .requestMatchers(HttpMethod.DELETE, "/aurafarm/vendas/**").hasRole("GERENTE")
                .requestMatchers("/aurafarm/vendas/**").authenticated()
                // Dashboard (RF018): Gerente e Funcionário
                .requestMatchers("/aurafarm/dashboard/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
