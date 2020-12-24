package com.example.demo.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Columns {
	private String thumbnailImageUrl;
	private String imageAspectRatio;
	private String imageSize;
	private String imageBackgroundColor;
	private String title;
	private String text;
	private DefaultAction defaultAction;
	private List<Actions> actions;
}
