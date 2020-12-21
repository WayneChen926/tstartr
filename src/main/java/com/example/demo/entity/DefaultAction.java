package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class DefaultAction {
	private String type;
	private String label;
	private String uri;
}
