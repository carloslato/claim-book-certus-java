package com.certus.claimbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests((requests) -> requests
                // 1. Rutas Públicas - El índice, CSS, etc., siempre públicos.
                .requestMatchers("/", "/css/**", "/img/**").permitAll() 
                
                // 2. LA CLAVE: Todas las rutas de API son públicas (o se autenticarán de otra forma)
                .requestMatchers("/api/**").permitAll()
                // 2. LA RUTA PROTEGIDA: Cualquier cosa que empiece con /reclamos/admin/
                //    requerirá que el usuario esté autenticado.
                .requestMatchers("/reclamos/admin/**").authenticated() 

                // 3. El resto de las rutas (ej. /reclamos/view, /contact) ahora son públicas por defecto.
                .anyRequest().permitAll() 
            )
            .formLogin((form) -> form
                .permitAll()
                .defaultSuccessUrl("/reclamos/admin/listar", true)
            )
            .logout((logout) -> logout.permitAll());
        
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // 3. Usuario en memoria para pruebas rápidas (sin BD)
        UserDetails user = User.withDefaultPasswordEncoder() // Solo para DEV
            .username("admin")
            .password("1234")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
}