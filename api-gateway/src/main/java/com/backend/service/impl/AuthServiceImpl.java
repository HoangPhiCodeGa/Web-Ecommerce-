package com.backend.service.impl;

import com.backend.dto.reponse.IntrospectResponse;
import com.backend.dto.request.IntrospectRequest;
import com.backend.repository.IdentityClient;
import com.backend.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    IdentityClient identityClient;

    @Override
    public Mono<ResponseEntity<IntrospectResponse>> isTokenValid(String token) {
        return identityClient.introspect(
                IntrospectRequest.builder()
                        .token(token)
                        .build());
    }
}
