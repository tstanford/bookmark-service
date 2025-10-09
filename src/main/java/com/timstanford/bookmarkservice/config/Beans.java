package com.timstanford.bookmarkservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    @Bean
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }
}
