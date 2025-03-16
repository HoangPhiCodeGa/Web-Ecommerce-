package com.backend.service;

import com.backend.dto.reponse.IntrospectResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<IntrospectResponse>> isTokenValid(String token);
}
