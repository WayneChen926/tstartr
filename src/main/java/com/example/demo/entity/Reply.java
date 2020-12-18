package com.example.demo.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Reply {
	private String replyToken;
	private List<?> messages;
}
