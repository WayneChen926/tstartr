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

import com.example.demo.entity.EntryWrapper;
import com.example.demo.entity.Entrys;
import com.example.demo.entity.Message;
import com.example.demo.entity.Messaging;
import com.example.demo.entity.Recipient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FacebookMessage {

	@Value("${facebook.messages.pageAccessToken}")
	private String pageAccessToken;

	@Autowired
	EntryWrapper entryWrapper;

	ObjectMapper objectMapper = new ObjectMapper();

	RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/webhook")
	public ResponseEntity<String> webhookVerify(
	        @RequestParam("hub.mode") String mode,
			@RequestParam("hub.verify_token") String verifyToken, 
			@RequestParam("hub.challenge") String challenge) {
	    
		if (mode.equals("subscribe") && verifyToken.equals("waynechen0926")) {
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		}
	}

	@PostMapping("/webhook")
	public ResponseEntity<String> webhook(@RequestBody String body)
			throws JsonMappingException, JsonProcessingException {
		log.debug("{}", body);
		System.out.println(body);
		String url = "https://graph.facebook.com/v9.0/me/messages?access_token=" + pageAccessToken;
		System.out.println(url);
		entryWrapper = objectMapper.readValue(body, EntryWrapper.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		String psid = null;
		String text = null;

		System.out.println(objectMapper.writeValueAsString(entryWrapper));

		Messaging messaging2 = null;

		for (Entrys entry : entryWrapper.getEntry()) {
			for (Messaging messaging : entry.getMessaging()) {
				psid = messaging.getSender().getId();
				System.out.println("PSID = " + psid);
				messaging2 = messaging;
				if (messaging.getRead() == null) {
					text = messaging.getMessage().getText();
				}
			}
		}
		if (messaging2.getMessage() != null) {
			if (!messaging2.getMessage().getText().equals("測試")) {
				Messaging messaging = new Messaging();
				Recipient recipient = new Recipient();
				Message message = new Message();
				messaging.setMessaging_type("RESPONSE");
				recipient.setId(psid);
				message.setText("測試");
				messaging.setRecipient(recipient);
				messaging.setMessage(message);
				System.out.println(objectMapper.writeValueAsString(messaging));
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
						new HttpEntity<>(objectMapper.writeValueAsString(messaging), headers), String.class);
				return response;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
