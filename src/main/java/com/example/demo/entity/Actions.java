package com.example.demo.entity;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Actions {
	private String type;
	private String label;
	private String data;
	private String uri;
}
