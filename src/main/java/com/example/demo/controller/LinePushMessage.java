package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Event;
import com.example.demo.entity.EventWrapper;
import com.example.demo.entity.TextMessages;
import com.example.demo.entity.PushMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LinePushMessage {

	@Autowired
	PushMessage pushmessage;
	
	@Autowired
	TextMessages messages;
	
	@Value("${spring.boot.admin.notify.line.channelToken}")
	private String channelToken;

	@PostMapping("/push")
	public void push(@RequestBody EventWrapper events) throws JsonProcessingException {
		String push = null;
		String to = null;
		List<TextMessages> lms = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		
		for(Event event:events.getEvents()){
			to = event.getSource().getUserId();
		}
		messages.setType("text");
		messages.setText("Hello 歡迎");
		lms.add(messages);
		pushmessage.setTo(to);
		pushmessage.setMessages(lms);
		
		push = objectMapper.writeValueAsString(pushmessage);
		System.out.println(push);
//		String defalut = "{\"to\":\""+to+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+"大家好"+"\"}]}";
		
		String url = "https://api.line.me/v2/bot/message/push";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer {" + channelToken + "}");

		HttpEntity<String> entity = new HttpEntity<>(push, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
		System.out.println(response.getBody());
	}
}
