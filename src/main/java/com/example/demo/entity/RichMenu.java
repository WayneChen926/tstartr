package com.example.demo.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RichMenu {
	private Size size;
	private boolean selected;
	private String name;
	private String chatBarText;
	private List<Areas> areas;
}
