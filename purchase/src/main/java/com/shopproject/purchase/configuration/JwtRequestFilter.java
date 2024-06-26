package com.shopproject.purchase.configuration;

import com.shopproject.purchase.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    protected void getToken(){

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String login = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                login = jwtTokenUtils.getLogin(jwt);
            } catch (ExpiredJwtException e) {
                log.debug("Время жизни токена вышло");
                throw e;
            } catch (Exception e) {
                log.debug("Произошла ошибка при работе токена");
                throw e;
            }
        }
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String id = jwtTokenUtils.getId(jwt);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    jwt,
                    id,
                    null
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

}