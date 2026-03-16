package com.example.library.config;

import com.example.library.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/persons").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person").permitAll()
                        .requestMatchers(HttpMethod.GET, "/book/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/profile/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/genres").permitAll()
                        .requestMatchers(HttpMethod.GET, "/favourites/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/borrow").permitAll()
                        .requestMatchers(HttpMethod.POST, "/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/return").permitAll()
                        .requestMatchers(HttpMethod.POST, "/save-book").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/save-books").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/favourites/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/update-profile").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/book").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/book").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        //config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedOrigins(List.of(frontendUrl));
        //config.setAllowedOrigins(List.of("*")); // RestControllerist võin ära võtta @CrossOrigin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
