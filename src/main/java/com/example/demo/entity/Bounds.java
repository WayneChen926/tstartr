package com.example.demo.entity;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Bounds {
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
}
