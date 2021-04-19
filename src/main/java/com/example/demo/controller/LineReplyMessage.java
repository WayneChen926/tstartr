package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventWrapper;
import com.example.demo.util.ResponseMessage;
import com.example.demo.util.VerificationLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LineReplyMessage {

    @Autowired
    VerificationLine verificationLine;

    @Autowired
    Event event;

    @Autowired
    ResponseMessage responseMessage;

    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/callback")
    public ResponseEntity<String> reply(@RequestBody String requestBody,
            @RequestHeader("X-Line-Signature") String line_headers) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String reply = "https://api.line.me/v2/bot/message/reply";
        String replyToken = null;
        EventWrapper events = objectMapper.readValue(requestBody, EventWrapper.class);
        log.info("{}", requestBody);
        // ------------------- User Evnet -----------------
        for (Event event : events.getEvents()) {
            replyToken = event.getReplyToken();
            this.event = event;
            log.trace("JSON:\n" + event);
            log.trace("message:" + event.getMessage());
        }
        log.trace("requestBody={}",requestBody);

        // 驗證 是否為line 傳過來的訊息
        if (verificationLine.checkFromLine(requestBody, line_headers)) {
            log.trace("驗證成功");
            if (event.getMessage() != null) {
                ResponseEntity<String> response = restTemplate.exchange(reply, HttpMethod.POST,
                        responseMessage.sendMessage(event.getMessage(), replyToken), String.class);
                return response;
            } else {

                return new ResponseEntity<String>(HttpStatus.OK);
            }
        } else {
            log.error("驗證失敗");
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }
}
