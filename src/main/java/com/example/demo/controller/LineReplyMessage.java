package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

import com.example.demo.entity.Actions;
import com.example.demo.entity.DefaultAction;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventWrapper;
import com.example.demo.entity.ImageMessages;
import com.example.demo.entity.LocationMessage;
import com.example.demo.entity.Message;
import com.example.demo.entity.Postback;
import com.example.demo.entity.Reply;
import com.example.demo.entity.StickerMessages;
import com.example.demo.entity.Template;
import com.example.demo.entity.TemplateMessages;
import com.example.demo.entity.TextMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LineReplyMessage {

	@Value("${spring.boot.admin.notify.line.channelToken}")
	private String channelToken;
	@Value("${spring.boot.admin.notify.line.channelSecret}")
	private String channelSecret;

	@Value("${line.messages.originalContentUrl}")
	String originalContentUrl;
	@Value("${line.messages.previewImageUrl}")
	String previewImageUrl;

	@Value("${line.messages.thumbnailImageUrl}")
	String thumbnailImageUrl;

	@Value("${line.messages.uri}")
	String uri;

	@Autowired
	Reply reply;

	@Autowired
	Message message;
	@Autowired
	Postback postback;

	@Autowired
	TextMessages textMessages;

	@Autowired
	StickerMessages stickerMessages;

	@Autowired
	ImageMessages imageMessages;

	@Autowired
	TemplateMessages templateMessages;

	@Autowired
	LocationMessage locationMessage;

	@Autowired
	Template template;

	@Autowired
	DefaultAction defaultAction;

	// 物件轉JSON
	ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping("/callback")
	public ResponseEntity<String> reply(@RequestBody String requestBody,
			@RequestHeader("X-Line-Signature") String line_headers) throws JsonProcessingException {

		String reply = "https://api.line.me/v2/bot/message/reply";
		String replyToken = null;
		EventWrapper events = objectMapper.readValue(requestBody, EventWrapper.class);
		
//    ------------------- User Evnet -----------------
		for (Event event : events.getEvents()) {
			replyToken = event.getReplyToken();
			message = event.getMessage();
			System.out.println("JSON:\n" + event);
			System.out.println("message:" + message);
		}

//    -------------------- JSON Data Text -----------------------------------
//		String defalut = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+"無法判斷"+"\"}]}";
//		String text = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"text\",\"text\":\""+message+"\"}]}";
//		String sticker = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"sticker\",\"packageId\":\""+11537+"\",\"stickerId\":\""+52002734+"\"}]}";
//		String image = "{\"replyToken\":\""+replyToken+"\",\"messages\":[{\"type\":\"image\",\"originalContentUrl\":\""+originalContentUrl+"\",\"previewImageUrl\":\""+previewImageUrl+"\"}]}";;

		RestTemplate restTemplate = new RestTemplate();
		if (checkFromLine(requestBody, line_headers)) {
			System.out.println("驗證成功");			
			if (message != null) {
				ResponseEntity<String> response = restTemplate.exchange(reply, HttpMethod.POST,
						sendMessage(message, replyToken), String.class);
				return response;
			} else {
				
				return null;
			}

		}else {
			System.out.println("驗證失敗");
			return null;
		}
	}

	public HttpEntity<String> sendMessage(Message message, String replyToken) throws JsonProcessingException {
		List<TextMessages> textList = new ArrayList<>();
		List<StickerMessages> stickerList = new ArrayList<>();
		List<ImageMessages> imageList = new ArrayList<>();
		List<TemplateMessages> templateList = new ArrayList<>();
		List<LocationMessage> locationList = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer {" + channelToken + "}");

		if (message.getType().equals("sticker")) {
//		      ------------------- 貼圖JSON -----------------------
			stickerMessages.setType("sticker");
			stickerMessages.setPackageId("11537");
			stickerMessages.setStickerId("52002734");
			stickerList.add(stickerMessages);

			reply.setReplyToken(replyToken);
			reply.setMessages(stickerList);

			return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

		} else if (message.getType().equals("text")) {
//		  	  ------------------- 文字JSON -------------------
			if (message.getText().equals("表單")) {

				templateMessages.setType("template");
				templateMessages.setAltText("This is a buttons template");

				template.setType("buttons");
				template.setThumbnailImageUrl(thumbnailImageUrl);
				template.setImageAspectRatio("rectangle");
				template.setImageSize("cover");
				template.setImageBackgroundColor("#FFFFFF");
				template.setTitle("貼圖");
				template.setText("Please select");

				defaultAction.setType("uri");
				defaultAction.setLabel("View detail");
				defaultAction.setUri(uri);

				Actions actions = new Actions();
				actions.setType("postback");
				actions.setLabel("Buy");
				actions.setData("測試購買");
				Actions actions1 = new Actions();
				actions1.setType("postback");
				actions1.setLabel("Add to cart");
				actions1.setData("測試加入購物車");
				Actions actions2 = new Actions();
				actions2.setType("uri");
				actions2.setLabel("View detail");
				actions2.setUri(uri);
				List<Actions> actionsList = new ArrayList<>();
				actionsList.add(actions);
				actionsList.add(actions1);
				actionsList.add(actions2);

				template.setActions(actionsList);
				template.setDefaultAction(defaultAction);
				templateMessages.setTemplate(template);
				templateList.add(templateMessages);

				reply.setReplyToken(replyToken);
				reply.setMessages(templateList);

				return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

			} else if (message.getText().equals("地圖")) {
				locationMessage.setType("location");
				locationMessage.setTitle("光華商場");
				locationMessage.setAddress("100台北市中正區八德路一段");
				locationMessage.setLatitude(new BigDecimal(25.04524731439183));
				locationMessage.setLongitude(new BigDecimal(121.53196609551186));
				locationList.add(locationMessage);

				reply.setReplyToken(replyToken);
				reply.setMessages(locationList);

				return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);
			} else {
				textMessages.setType("text");
				textMessages.setText("收到您的訊息: " + message.getText());
				textList.add(textMessages);

				reply.setReplyToken(replyToken);
				reply.setMessages(textList);

				return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);
			}
		} else if (message.getType().equals("image")) {
//		------------------- image JSON -------------------
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

//	驗證 是否為line 傳過來的訊息
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

			e.printStackTrace();
		}

		return false;
	}
}
