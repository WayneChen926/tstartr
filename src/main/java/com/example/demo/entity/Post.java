package com.example.demo.entity;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private String status_type;
    private boolean is_published;
    private String updated_time;
    private String permalink_url;
    private String promotion_status;
    private String id;
}
