package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@RestController
public class LineChat {

	@Value("${spring.boot.admin.notify.line.channelToken}")
	private String channelToken;
	
	@PostMapping("/callback")
	public void reply(@RequestBody EventWrapper events) {
		String replyToken = null;
		String message = null;
		for(Event event:events.getEvents()){
			replyToken = event.getReplyToken();
			message = event.getMessage().getText();
		}

		String body = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+message+"\"}]}";
		System.out.println(body);
		String url = "https://api.line.me/v2/bot/message/reply";
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    headers.add("Authorization", "Bearer {" + channelToken +"}");
	    HttpEntity<String> entity = new HttpEntity<>(body,headers);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);

        System.out.println(response.getBody());
	}
}
