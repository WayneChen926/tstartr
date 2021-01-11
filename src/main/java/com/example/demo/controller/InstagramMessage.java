package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entity.Changes;
import com.example.demo.entity.EntryWrapper;
import com.example.demo.entity.Entrys;
import com.example.demo.entity.Values;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class InstagramMessage {

    @Value("${facebook.messages.pageAccessToken}")
    private String pageAccessToken;

    @Autowired
    EntryWrapper entryWrapper;

    @Autowired
    Changes change;

    ObjectMapper objectMapper = new ObjectMapper();

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/webhookig")
    public String webhookVerify(@RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam("hub.challenge") String challenge) {
        log.info("{}", challenge);
        if (mode.equals("subscribe") && verifyToken.equals("waynechen0926")) {
            return challenge;
        } else {
            return null;
        }
    }

    @PostMapping("/webhookig")
    public ResponseEntity<String> webhook(@RequestBody String body)
            throws JsonMappingException, JsonProcessingException {
        log.debug("{}", body);
        System.out.println(body);
        // System.out.println(url);
        entryWrapper = objectMapper.readValue(body, EntryWrapper.class);
        System.out.println("OJM:" + objectMapper.writeValueAsString(entryWrapper));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Entrys entry : entryWrapper.getEntry()) {
            if (entry.getChanges() != null) {
                for (Changes change : entry.getChanges()) {
                    this.change = change;
                }
            }
        }
        try {
            if (change != null&&!change.getValue().getText().equals("bot test 感謝留言")) {
                String url = "https://graph.facebook.com/v9.0/" + change.getValue().getId()
                        + "/replies?access_token=" + pageAccessToken;

                Values value = new Values();
                value.setMessage("bot test 感謝留言");

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
                        new HttpEntity<>(objectMapper.writeValueAsString(value), headers),
                        String.class);
                return response;
            } else {
                return new ResponseEntity<String>("null", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("null", HttpStatus.OK);
        }
    }
}
