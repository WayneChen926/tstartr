package com.example.demo.entity;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class LocationMessage {
	private String type;
	private String title;
	private String address;
	private BigDecimal  latitude;
	private BigDecimal  longitude;
}
