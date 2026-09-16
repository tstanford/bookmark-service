package com.timstanford.bookmarkservice.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Configuration
public class BeanConfig {

    @Bean
    public YAMLMapper getYamlMapper(){
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        var mapper = new YAMLMapper(yamlFactory);
        mapper.setDefaultPropertyInclusion(NON_NULL);
        return mapper;
    }
}
