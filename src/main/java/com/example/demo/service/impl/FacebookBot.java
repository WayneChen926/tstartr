package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entity.Attachments;
import com.example.demo.entity.Buttons;
import com.example.demo.entity.Changes;
import com.example.demo.entity.DefaultAction;
import com.example.demo.entity.Elements;
import com.example.demo.entity.EntryWrapper;
import com.example.demo.entity.Entrys;
import com.example.demo.entity.Message;
import com.example.demo.entity.Messaging;
import com.example.demo.entity.Payload;
import com.example.demo.entity.Quick_replies;
import com.example.demo.entity.Recipient;
import com.example.demo.entity.Values;
import com.example.demo.service.FacebookBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacebookBot implements FacebookBotService {
    @Value("${facebook.messages.pageAccessToken}")
    private String pageAccessToken;

    @Autowired
    Entrys entry;

    @Autowired
    Changes change;

    @Autowired
    EntryWrapper entryWrapper;

    ObjectMapper objectMapper = new ObjectMapper();

    RestTemplate restTemplate = new RestTemplate();


    @Override
    public ResponseEntity<String> message(String body) {
        log.debug("{}", body);
        String url = "https://graph.facebook.com/v9.0/me/messages?access_token=" + pageAccessToken;
        // System.out.println(url);
        HttpHeaders headers = null;
        Messaging messaging2 = null;
        try {
            entryWrapper = objectMapper.readValue(body, EntryWrapper.class);
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            messaging2 = null;


            log.debug("OJM:", objectMapper.writeValueAsString(entryWrapper));
        } catch (JsonMappingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JsonProcessingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        for (Entrys entry : entryWrapper.getEntry()) {
            this.entry = entry;
            if (entry.getMessaging() != null) {
                for (Messaging messaging : entry.getMessaging()) {
                    messaging2 = messaging;
                }
            }
            if (entry.getChanges() != null) {
                for (Changes change : entry.getChanges()) {
                    this.change = change;
                }
            }
        }
        try {
            if (messaging2 != null) {
                if (messaging2.getRecipient().getId() != "2059605444295770"
                        && messaging2.getMessage() != null) {


                    if (messaging2.getMessage().getText().equals("快")) {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();
                        List<Quick_replies> quick_replies_list = new ArrayList<>();
                        Quick_replies quick_replies = new Quick_replies();
                        Quick_replies quick_replies2 = new Quick_replies();

                        recipient.setId(messaging2.getSender().getId());

                        messaging.setMessaging_type("RESPONSE");
                        message.setText("Pick a color:");
                        quick_replies.setContent_type("text");
                        quick_replies.setTitle("Red");
                        quick_replies.setPayload(url);
                        quick_replies.setImage_url(
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7SMAfDBSxwNRp9SX4GK840rz0w7TCXHgUHg&usqp=CAU");

                        quick_replies2.setContent_type("text");
                        quick_replies2.setTitle("Blue");
                        quick_replies2.setPayload(url);
                        quick_replies2.setImage_url(
                                "https://upload.wikimedia.org/wikipedia/commons/d/d5/Icon_Transparent_Blue.png");

                        quick_replies_list.add(quick_replies);
                        quick_replies_list.add(quick_replies2);

                        message.setQuick_replies(quick_replies_list);
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        // System.out.println("快還要更快!!!!!!!!!!!!! = "
                        // + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;
                    } else if (messaging2.getMessage().getText().equals("表單")) {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();
                        Attachments attachment = new Attachments();
                        Payload payload = new Payload();
                        Elements elements = new Elements();
                        DefaultAction default_action = new DefaultAction();
                        Buttons buttons = new Buttons();
                        Buttons buttons2 = new Buttons();
                        List<Elements> elementList = new ArrayList<>();
                        List<Buttons> buttonList = new ArrayList<>();

                        recipient.setId(messaging2.getSender().getId());
                        attachment.setType("template");
                        payload.setTemplate_type("generic");
                        elements.setTitle("歡迎來到白爛貓商店");
                        elements.setImage_url(
                                "https://shop.7-11.com.tw/mdz_file/item/31/01/04/1806/18060003701G_char_5_180613184332.jpg");
                        elements.setSubtitle("更多的商品資訊請點進來看");
                        default_action.setType("web_url");
                        default_action.setUrl("https://lanlancatshop.that-fish.com/");
                        default_action.setMessenger_extensions(true);
                        default_action.setWebview_height_ratio("tall");
                        default_action.setFallback_url(
                                "https://tickets.books.com.tw/progshow/08010201468934?utm_source=wenk&utm_medium=googlesearch&utm_campaign=lanlancat");
                        elements.setDefault_action(default_action);

                        buttons.setType("web_url");
                        buttons.setUrl("https://lanlancatshop.that-fish.com/");
                        buttons.setTitle("商城頁面");

                        buttons2.setType("postback");
                        buttons2.setTitle("測試文字");
                        buttons2.setPayload(url);
                        buttonList.add(buttons);
                        buttonList.add(buttons2);
                        elements.setButtons(buttonList);

                        elementList.add(elements);
                        payload.setElements(elementList);
                        attachment.setPayload(payload);

                        message.setAttachment(attachment);
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        System.out.println(
                                "表單 !!!!!!!!!!!!! = " + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;

                    } else if (messaging2.getRecipient().getId() != "2059605444295770") {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();

                        messaging.setMessaging_type("RESPONSE");
                        recipient.setId(messaging2.getSender().getId());
                        message.setText(messaging2.getMessage().getText());
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        // System.out.println(
                        // "TEXT!!!!!!!!!!!!! =" + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;
                    } else {
                        return new ResponseEntity<String>("null", HttpStatus.OK);
                    }
                } else if (messaging2.getPostback() != null) {
                    if (messaging2.getRecipient().getId() != "2059605444295770"
                            && messaging2.getPostback().getTitle().equals("喜歡玩的遊戲")) {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();

                        messaging.setMessaging_type("RESPONSE");
                        recipient.setId(messaging2.getSender().getId());
                        message.setText("Dark Souls 黑暗靈魂系列 和 互動式電影類以及RPG遊戲");
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        System.out.println(
                                "TEXT!!!!!!!!!!!!! =" + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;
                    } else if (messaging2.getRecipient().getId() != "2059605444295770"
                            && messaging2.getPostback().getTitle().equals("開台時間")
                            || messaging2.getPostback().getTitle().equals("開台資訊")) {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();

                        messaging.setMessaging_type("RESPONSE");
                        recipient.setId(messaging2.getSender().getId());
                        message.setText("每周三、五晚上");
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        System.out.println(
                                "TEXT!!!!!!!!!!!!! =" + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;
                    }

                    else if (messaging2.getRecipient().getId() != "2059605444295770"
                            && messaging2.getPostback().getTitle().equals("你有故事嗎 我有茶")) {
                        Messaging messaging = new Messaging();
                        Recipient recipient = new Recipient();
                        Message message = new Message();

                        messaging.setMessaging_type("RESPONSE");
                        recipient.setId(messaging2.getSender().getId());
                        message.setText("把酒言歡，何樂不為");
                        messaging.setRecipient(recipient);
                        messaging.setMessage(message);

                        // System.out.println(
                        // "TEXT!!!!!!!!!!!!! =" + objectMapper.writeValueAsString(messaging));
                        ResponseEntity<String> response =
                                restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(objectMapper.writeValueAsString(messaging),
                                                headers),
                                        String.class);
                        return response;
                    } else {
                        return new ResponseEntity<String>(HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<String>(HttpStatus.OK);
                }
            } else if (!change.getValue().getFrom().getId().equals("2059605444295770")
                    && entry.getChanges() != null && change.getValue().getVerb().equals("add")
                    && change.getValue().getItem().equals("comment")) {
                String pageReply = "https://graph.facebook.com/" + change.getValue().getComment_id()
                        + "/comments?" + "access_token=" + pageAccessToken;
                Values value = new Values();
                value.setMessage("留言回覆測試");
                ResponseEntity<String> response = restTemplate.exchange(pageReply, HttpMethod.POST,
                        new HttpEntity<>(objectMapper.writeValueAsString(value), headers),
                        String.class);
                return response;

            } else if (!change.getValue().getFrom().getId().equals("2059605444295770")
                    && entry.getChanges() != null && change.getValue().getVerb().equals("add")) {
                String pageReply = "https://graph.facebook.com/" + change.getValue().getPost_id()
                        + "/comments?" + "access_token=" + pageAccessToken;
                Values value = new Values();
                value.setMessage("貼文回覆測試");
                ResponseEntity<String> response = restTemplate.exchange(pageReply, HttpMethod.POST,
                        new HttpEntity<>(objectMapper.writeValueAsString(value), headers),
                        String.class);
                return response;
            } else {
                return new ResponseEntity<String>(HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }


}
