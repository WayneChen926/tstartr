package com.example.demo.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ReplyWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<Reply> reply;

}
