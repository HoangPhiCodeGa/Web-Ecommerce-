package com.backend.aichatbox.controller;

import com.backend.aichatbox.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class MyController {
    ChatService chatClient;
    @PostMapping
    public ResponseEntity<String> getChatResponse(@RequestBody Map<String, String> message) {
        String question = message.get("question");
        String response = chatClient.getChatResponse(question);

        return ResponseEntity.ok(response);
    }
}
