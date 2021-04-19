package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TstartrApplication extends SpringBootServletInitializer {
    
    // 外部tomcat 佈署設定檔
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TstartrApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TstartrApplication.class, args);
    }
}
