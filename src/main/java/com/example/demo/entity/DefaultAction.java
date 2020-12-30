package com.example.demo.entity;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultAction {
	private String type;
	private String label;
	private String uri;
	
//	FB
	private String url;
	private boolean messenger_extensions;
	private String webview_height_ratio;
	private String fallback_url;
}
