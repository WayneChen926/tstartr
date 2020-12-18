package com.example.demo.entity;

import lombok.Data;

@Data
public class Event {
	
	private String replyToken;
    private String type;
    private String timestamp;
    private Source source;
    private Message message;
    
}
