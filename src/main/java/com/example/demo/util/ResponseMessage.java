package com.example.demo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Actions;
import com.example.demo.entity.Columns;
import com.example.demo.entity.DefaultAction;
import com.example.demo.entity.ImageMessages;
import com.example.demo.entity.LocationMessage;
import com.example.demo.entity.Message;
import com.example.demo.entity.Postback;
import com.example.demo.entity.Reply;
import com.example.demo.entity.RichMenu;
import com.example.demo.entity.StickerMessages;
import com.example.demo.entity.Template;
import com.example.demo.entity.TemplateMessages;
import com.example.demo.entity.TextMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseMessage {

    @Value("${spring.boot.admin.notify.line.channelToken}")
    private String channelToken;

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

    // Jackson ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();

    public HttpEntity<String> sendMessage(Message message, String replyToken)
            throws JsonProcessingException {
        List<TextMessages> textList = new ArrayList<>();
        List<StickerMessages> stickerList = new ArrayList<>();
        List<ImageMessages> imageList = new ArrayList<>();
        List<TemplateMessages> templateList = new ArrayList<>();
        List<LocationMessage> locationList = new ArrayList<>();
        List<Columns> columnsList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer {" + channelToken + "}");

        if (message.getType().equals("sticker")) {
            // ------------------- 貼圖JSON -----------------------
            stickerMessages.setType("sticker");
            stickerMessages.setPackageId(Integer.toString((int) (Math.random() * 3) + 11537));
            if (stickerMessages.getPackageId().equals("11537")) {
                stickerMessages
                        .setStickerId(Integer.toString((int) (Math.random() * 40) + 52002734));
            } else if (stickerMessages.getPackageId().equals("11538")) {
                stickerMessages
                        .setStickerId(Integer.toString((int) (Math.random() * 40) + 51626494));
            } else if (stickerMessages.getPackageId().equals("11539")) {
                stickerMessages
                        .setStickerId(Integer.toString((int) (Math.random() * 40) + 52114110));
            } else {
                System.out.println("error");
            }
            stickerList.add(stickerMessages);

            reply.setReplyToken(replyToken);
            reply.setMessages(stickerList);

            return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);

        } else if (message.getType().equals("text")) {
            // ------------------- template JSON -------------------
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
                // ------------------- location JSON -------------------
                locationMessage.setType("location");
                locationMessage.setTitle("光華商場");
                locationMessage.setAddress("100台北市中正區八德路一段");
                locationMessage.setLatitude(new BigDecimal(25.04524731439183));
                locationMessage.setLongitude(new BigDecimal(121.53196609551186));
                locationList.add(locationMessage);

                reply.setReplyToken(replyToken);
                reply.setMessages(locationList);

                return new HttpEntity<>(objectMapper.writeValueAsString(reply), headers);
            } else if (message.getText().equals("購物清單")) {

                templateMessages.setType("template");
                templateMessages.setAltText("This is a buttons template");
                template.setType("carousel");
                template.setImageAspectRatio("rectangle");
                template.setImageSize("cover");
                Columns columns = new Columns();
                columns.setThumbnailImageUrl(
                        "https://image-cdn-flare.qdm.cloud/q591070f643292/image/cache/data/2019/10/06/2601298eca2cb7c7ee42a12fdd00cbd7-max-w-1024.jpg");
                columns.setImageBackgroundColor("#FFFFFF");
                columns.setTitle("攝影視覺行銷＋手機攝影技巧教學");
                columns.setText("更多資訊請點進來");

                defaultAction.setType("uri");
                defaultAction.setLabel("View detail");
                defaultAction.setUri("https://www.howhowphoto.com/product/product&product_id=315");
                columns.setDefaultAction(defaultAction);

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
                columns.setActions(actionsList);
                columnsList.add(columns);



                Columns columns2 = new Columns();
                columns2.setThumbnailImageUrl(thumbnailImageUrl);
                columns2.setImageBackgroundColor("#000000");
                columns2.setTitle("貼書商城");
                columns2.setText("查看更多的貼圖");
                DefaultAction defaultAction2 = new DefaultAction();
                defaultAction2.setType("uri");
                defaultAction2.setLabel("View detail");
                defaultAction2.setUri(uri);
                columns2.setDefaultAction(defaultAction2);

                Actions actions3 = new Actions();
                actions3.setType("postback");
                actions3.setLabel("Buy");
                actions3.setData("測試購買");
                Actions actions4 = new Actions();
                actions4.setType("postback");
                actions4.setLabel("Add to cart");
                actions4.setData("測試加入購物車");
                Actions actions5 = new Actions();
                actions5.setType("uri");
                actions5.setLabel("View detail");
                actions5.setUri(uri);

                List<Actions> actionsList2 = new ArrayList<>();
                actionsList2.add(actions3);
                actionsList2.add(actions4);
                actionsList2.add(actions5);
                columns2.setActions(actionsList2);
                columnsList.add(columns2);



                template.setColumns(columnsList);
                templateMessages.setTemplate(template);
                templateList.add(templateMessages);

                reply.setReplyToken(replyToken);
                reply.setMessages(templateList);

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
            // ------------------- image JSON -------------------
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

    public HttpEntity<String> richmenu(RichMenu json) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer {" + channelToken + "}");

        return new HttpEntity<>(objectMapper.writeValueAsString(json), headers);
    }
}
