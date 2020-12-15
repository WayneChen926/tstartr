package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Webhook {
	
	@PostMapping("/webhook")
	@ResponseBody
	public String postWebhook(HttpServletRequest req,HttpServletResponse res) {
		
		
		return null;
	}
	
	@GetMapping("/webhook")
	@ResponseBody
	public String getWebhook(HttpServletRequest req,HttpServletResponse res) {
		
		
		return null;
	}
}
