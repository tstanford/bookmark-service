package com.timstanford.bookmarkservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
public class BeanConfig {
    @Bean
    @Primary
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }

    @Bean
    @Qualifier("yamlmapper")
    public ObjectMapper getYamlMapper(){
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        return new ObjectMapper(yamlFactory)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
