package com.example.demo.entity;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messaging {
	private String messaging_type;
	private Sender sender;
	private Recipient recipient;
	private String timestamp;
	private Read read;
	private Message message;
}
