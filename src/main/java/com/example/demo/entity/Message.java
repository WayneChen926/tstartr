package com.example.demo.entity;

import lombok.Data;

@Data
public class Message {
	private String id;
    private String type;
    private String text;
    private String filename;
    private String filesize;
    private String title;
    private String address;
    private String latitude;
    private String longitude;
    private String packageId;
    private String stickerId;
}
