package com.example.demo.entity;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Values {
    private From from;
    private Post post;
    private boolean is_hidden;
    private String message;
    private String post_id;
    private long created_time;
    private String item;
    private String recipient_id;
    private String comment_id;
    private String parent_id;
    private String verb;
    
//    IG
    private String id;
    private String text;
}
