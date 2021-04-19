package com.example.demo.controller;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.FacebookBotService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FacebookMessage {



    @Autowired
    private FacebookBotService facebookBot;

    @GetMapping("/googleAppsScript")
    public String hello() {
        return "Call API " + new Timestamp(System.currentTimeMillis());
    }

    @GetMapping("/webhook")
    public String webhookVerify(@RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam("hub.challenge") String challenge) {
        
        log.trace("verifyToken = {}", verifyToken);
        if (mode.equals("subscribe") && verifyToken.equals("waynechen0926")) {
            return challenge;
        } else {
            return null;
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String body) {
        log.debug("{}", body);
        return facebookBot.message(body);
    }
}
