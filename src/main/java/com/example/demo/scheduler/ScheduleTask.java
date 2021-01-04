package com.example.demo.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduleTask {

    @Value("${spring.boot.admin.notify.line.channelToken}")
    private String channelToken;
    
    @Scheduled(cron = "*/25 9-21 1-5 * *")
    public void printSay() {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String body = "{\"events\":[],\"destination\":\"Ua63e48e33c432347d368a3a1b3023e72\"}";
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer {" + channelToken + "}");
        
        rt.exchange("https://messageapi997035.herokuapp.com/callback", 
                HttpMethod.POST, new HttpEntity<>(body,headers), String.class);
    }
    
}
