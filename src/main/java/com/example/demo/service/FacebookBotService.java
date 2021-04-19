package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FacebookBotService {
    
    public ResponseEntity<String> message(String body);
}
