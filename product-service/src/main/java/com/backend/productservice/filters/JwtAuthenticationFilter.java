/*
 * @(#) $(NAME).java    1.0     3/30/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.productservice.filters;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 30-March-2025 5:17 PM
 */

import com.backend.commonservice.model.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Nếu không có token, tiếp tục đến các filter khác
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = extractClaims(token);
            
            List<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles"))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, authorities
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            
        } catch (ExpiredJwtException e) {
            logger.error("JWT token đã hết hạn: {}", e.getMessage());
            sendErrorResponse(response, ErrorMessage.INVALID_TOKEN);
        } catch (JwtException e) {
            logger.error("Token không hợp lệ: {}", e.getMessage());
            sendErrorResponse(response, ErrorMessage.INVALID_TOKEN);
        } catch (Exception e) {
            logger.error("Lỗi xác thực: {}", e.getMessage());
            sendErrorResponse(response, ErrorMessage.UNAUTHENTICATED);
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, ErrorMessage errorMessage) throws IOException {
        response.setStatus(errorMessage.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", errorMessage.getCode());
        errorDetails.put("message", errorMessage.getMessage());
        
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
