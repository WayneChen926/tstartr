package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
public class TstartrApplication extends SpringBootServletInitializer {
    
    @Value("${spring.profiles.active}")
    private String active;
    
    // 外部tomcat 佈署設定檔
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TstartrApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TstartrApplication.class, args);
    }
    
    @RestController
    class checkHealth{
        
        @GetMapping
        public Map<String,String> check(){
            Map<String,String> m = new HashMap<>();
            m.put("check active", active);
            return m ;
        }
    }
}
