package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Actions;
import com.example.demo.entity.Areas;
import com.example.demo.entity.Bounds;
import com.example.demo.entity.RichMenu;
import com.example.demo.entity.Size;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class RichMenuTemplate {
	
	@Value("${spring.boot.admin.notify.line.channelToken}")
	private String channelToken;
	
	@Autowired
	RichMenu richMenu;
	
	@Autowired
	Size size;
	
	@Autowired
	ResponseMessage responseMessage;
	
	String richMenuURL = "https://api.line.me/v2/bot/richmenu";
	
	public void setting() throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
//		------------------------------------
		List <Areas> areasList = new ArrayList<>();
		
		size.setWidth(2500);
		size.setHeight(843);
		richMenu.setSize(size);
		richMenu.setSelected(false);
		richMenu.setName("richmenu-1");
		richMenu.setChatBarText("查看選單");
		
		Bounds bounds = new Bounds();
		bounds.setX(0);
		bounds.setY(0);
		bounds.setWidth(833);
		bounds.setHeight(843);
		Actions action = new Actions();
		action.setType("message");
		action.setLabel("文字");
		action.setText("Hello, iBot!");
		Areas areas = new Areas();
		areas.setBounds(bounds);
		areas.setAction(action);
		areasList.add(areas);
		
		Bounds bounds1 = new Bounds();
		bounds1.setX(833);
		bounds1.setY(0);
		bounds1.setWidth(833);
		bounds1.setHeight(843);
		Actions action1 = new Actions();
		action1.setType("uri");
		action1.setLabel("網址");
		action1.setUri("https://www.facebook.com/Wayne997035");
		Areas areas1 = new Areas();
		areas1.setBounds(bounds1);
		areas1.setAction(action1);
		areasList.add(areas1);
		
		Bounds bounds2 = new Bounds();
		bounds2.setX(1666);
		bounds2.setY(0);
		bounds2.setWidth(833);
		bounds2.setHeight(843);
		Actions action2 = new Actions();
		action2.setType("postback");
		action2.setLabel("選單2");
		action2.setData("changeMenu2");
		Areas areas2 = new Areas();
		areas2.setBounds(bounds2);
		areas2.setAction(action2);
		areasList.add(areas2);
		
		richMenu.setAreas(areasList);
		
		System.out.println("Text123456:"+responseMessage.richmenu(richMenu).getBody());
		
		String richMenuId = restTemplate.exchange(richMenuURL, HttpMethod.POST,
				 responseMessage.richmenu(richMenu), String.class).getBody();
		System.out.println(richMenuId);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer {" + channelToken + "}");
		
		String r[] = richMenuId.split("\"");
//		System.out.println(r[2]);
		String richDefault = "https://api.line.me/v2/bot/user/all/richmenu/"+r[3];
		System.out.println("richDefault = "+richDefault);
//		------------------------------------
	}
}
