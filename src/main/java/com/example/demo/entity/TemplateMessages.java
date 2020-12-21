package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TemplateMessages {
	private String type;
	private String altText;
	private Template Template;
}
