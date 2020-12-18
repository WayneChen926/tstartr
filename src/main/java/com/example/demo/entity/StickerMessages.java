package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class StickerMessages {
	private String type;
	private String packageId;
	private String stickerId;

}
