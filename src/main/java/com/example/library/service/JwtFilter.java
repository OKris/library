package com.example.library.service;

import com.example.library.entity.Person;
import com.example.library.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");
            Person person = jwtService.validateToken(token);

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (person.getRole().equals(Role.USER)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            }
            if (person.getRole().equals(Role.ADMIN)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(person.getId(), null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
