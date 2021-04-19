package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entity.Changes;
import com.example.demo.entity.EntryWrapper;
import com.example.demo.entity.Entrys;
import com.example.demo.entity.Values;
import com.example.demo.service.FacebookBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InstagramBot implements FacebookBotService {

    @Value("${facebook.messages.pageAccessToken}")
    private String pageAccessToken;

    @Autowired
    EntryWrapper entryWrapper;

    @Autowired
    Changes change;

    ObjectMapper objectMapper = new ObjectMapper();

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public ResponseEntity<String> message(String body) {
        log.debug("{}", body);
        try {
            entryWrapper = objectMapper.readValue(body, EntryWrapper.class);
            log.trace("OJM:" + objectMapper.writeValueAsString(entryWrapper));
        } catch (JsonMappingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JsonProcessingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
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
            if (change != null && !change.getValue().getText().equals("bot test 感謝留言")) {
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
