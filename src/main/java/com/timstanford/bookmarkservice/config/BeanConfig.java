package com.timstanford.bookmarkservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
public class BeanConfig {
    @Bean
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }
}
