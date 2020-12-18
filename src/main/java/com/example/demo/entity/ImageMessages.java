package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ImageMessages {
	private String type;
	private String originalContentUrl;
	private String previewImageUrl;
}
