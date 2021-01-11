package com.example.demo.scheduler;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduleTask {

    @Value("${spring.boot.admin.notify.line.channelToken}")
    private String channelToken;

    @Scheduled(cron = "0 */30 8-23 * * 1-5")
    public void printSay() {
        RestTemplate rt = new RestTemplate();
        try {
            rt.exchange("https://messageapi997035.herokuapp.com/googleAppsScript", HttpMethod.GET,
                    new HttpEntity<>("null"), String.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("排程執行完畢 " + new Timestamp(System.currentTimeMillis()));
        }
    }

}
