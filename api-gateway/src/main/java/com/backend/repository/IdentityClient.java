package com.backend.repository;

import com.backend.dto.reponse.IntrospectResponse;
import com.backend.dto.request.IntrospectRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;
public interface IdentityClient {
    @PostExchange(value = "/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
