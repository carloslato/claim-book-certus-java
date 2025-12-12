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

                .requestMatchers("/", "/css/**", "/img/**").permitAll() 
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/reclamos/admin/**").authenticated() 


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