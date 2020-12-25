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
import com.example.demo.entity.Message;
import com.example.demo.util.ResponseMessage;
import com.example.demo.util.VerificationLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LineReplyMessage {

    @Autowired
    Message message;

    @Autowired
    VerificationLine verificationLine;

    @Autowired
    ResponseMessage responseMessage;

    // @Autowired
    // RichMenuTemplate rt;

    // Jackson ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/callback")
    public ResponseEntity<String> reply(@RequestBody String requestBody,
            @RequestHeader("X-Line-Signature") String line_headers) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String reply = "https://api.line.me/v2/bot/message/reply";
        String replyToken = null;
        EventWrapper events = objectMapper.readValue(requestBody, EventWrapper.class);
        Event event2 = new Event();
        log.info("{}", requestBody);
        // ------------------- User Evnet -----------------
        for (Event event : events.getEvents()) {
            replyToken = event.getReplyToken();
            message = event.getMessage();
            event2 = event;
            System.out.println("JSON:\n" + event);
            System.out.println("message:" + message);
        }
        System.out.println(requestBody);
        // rt.setting();
        // -------------------- JSON Data Text -----------------------------------
        // String defalut =
        // "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+"無法判斷"+"\"}]}";
        // String text =
        // "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+message+"\"}]}";
        // String sticker =
        // "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"sticker\",\"packageId\":\""+11537+"\",\"stickerId\":\""+52002734+"\"}]}";
        // String image =
        // "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"image\",\"originalContentUrl\":\""+originalContentUrl+"\",\"previewImageUrl\":\""+previewImageUrl+"\"}]}";;

        // 驗證 是否為line 傳過來的訊息
        if (verificationLine.checkFromLine(requestBody, line_headers)) {
            System.out.println("驗證成功");
            if (event2.getMessage()!= null) {
                ResponseEntity<String> response = restTemplate.exchange(reply, HttpMethod.POST,
                        responseMessage.sendMessage(message, replyToken), String.class);
                return response;
            } else {

                return new ResponseEntity<String>("null", HttpStatus.OK);
            }
        } else {
            System.out.println("驗證失敗");
            return new ResponseEntity<String>("null", HttpStatus.OK);
        }
    }
}
