package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Areas {
	private Bounds bounds;
	private Actions action;
}
