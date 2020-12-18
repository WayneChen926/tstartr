package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Event;
import com.example.demo.entity.EventWrapper;
import com.example.demo.entity.ImageMessages;
import com.example.demo.entity.Reply;
import com.example.demo.entity.ReplyWrapper;
import com.example.demo.entity.StickerMessages;
import com.example.demo.entity.TextMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LineReplyMessage {

	@Value("${spring.boot.admin.notify.line.channelToken}")
	private String channelToken;
	@Value("${spring.boot.admin.notify.line.channelSecret}")
	private String channelSecret;

	@Value("${line.image.originalContentUrl}")
	String originalContentUrl;
	@Value("${line.image.previewImageUrl}")
	String previewImageUrl;

	@Autowired
	Reply reply;

	@Autowired
	TextMessages textMessages;

	@Autowired
	StickerMessages stickerMessages;

	@Autowired
	ImageMessages imageMessages;

	@Autowired
	ReplyWrapper replyWrapper;

	@PostMapping("/callback")
	public void reply(@RequestBody EventWrapper events, @RequestHeader("X-Line-Signature") String line_headers)
			throws JsonProcessingException {

		String reply = "https://api.line.me/v2/bot/message/reply";
		String replyToken = null;
		String type = null;

//    ------------------- User Evnet -----------------
		for (Event event : events.getEvents()) {
			replyToken = event.getReplyToken();
			type = event.getMessage().getType();
			System.out.println("JSON:\n" + event);

			System.out.println("Type:" + type);
		}

//    -------------------- JSON Data Text -----------------------------------
//		String defalut = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+"無法判斷"+"\"}]}";
//		String text = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+message+"\"}]}";
//		String sticker = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"sticker\",\"packageId\":\""+11537+"\",\"stickerId\":\""+52002734+"\"}]}";
//		String image = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"image\",\"originalContentUrl\":\""+originalContentUrl+"\",\"previewImageUrl\":\""+previewImageUrl+"\"}]}";;

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(reply, HttpMethod.POST, sendMessage(type, replyToken),
				String.class);

		System.out.println(response.getBody());
	}

	public HttpEntity<String> sendMessage(String type, String replyToken) throws JsonProcessingException {
		// 物件轉JSON
		ObjectMapper objectMapper = new ObjectMapper();
		List<TextMessages> textList = new ArrayList<>();
		List<StickerMessages> stickerList = new ArrayList<>();
		List<ImageMessages> imageList = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer {" + channelToken + "}");

		if (type.equals("sticker")) {
//		      ------------------- 貼圖JSON -----------------------
			stickerMessages.setType("sticker");
			stickerMessages.setPackageId("11537");
			stickerMessages.setStickerId("52002734");
			stickerList.add(stickerMessages);

			reply.setReplyToken(replyToken);
			reply.setMessages(stickerList);

			return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

		} else if (type.equals("text")) {
//		  	  ------------------- 文字JSON -------------------
			textMessages.setType("text");
			textMessages.setText("收到您的訊息");
			textList.add(textMessages);

			reply.setReplyToken(replyToken);
			reply.setMessages(textList);

			return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

		} else if (type.equals("image")) {
//			  	  ------------------- image JSON -------------------
			imageMessages.setType("image");
			imageMessages.setOriginalContentUrl(originalContentUrl);
			imageMessages.setPreviewImageUrl(previewImageUrl);
			imageList.add(imageMessages);

			reply.setReplyToken(replyToken);
			reply.setMessages(imageList);

			return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

		} else {
			textMessages.setType("text");
			textMessages.setText("無法辨識");
			textList.add(textMessages);

			reply.setReplyToken(replyToken);
			reply.setMessages(textList);

			return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);
		}
	}

	public boolean checkFromLine(String requestBody, String X_Line_Signature) {
		SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] source = requestBody.getBytes("UTF-8");
			String signature = Base64.encodeBase64String(mac.doFinal(source));
			if (signature.equals(X_Line_Signature)) {
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
