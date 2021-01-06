package com.example.demo.entity;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String type;
    private String replyToken;
    private String timestamp;
    private String mode;
    private Source source;
    private Message message;
    private Postback postback;
}
