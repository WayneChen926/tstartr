package com.example.demo.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Template {
	private String type;
	private String thumbnailImageUrl;
	private String imageAspectRatio;
	private String imageSize;
	private String imageBackgroundColor;
	private String title;
	private String text;
	private DefaultAction defaultAction;
	private List<Actions> actions;
}
