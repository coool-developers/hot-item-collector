package com.sparta.hotitemcollector.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://13.125.60.206:63342", "http://13.125.60.206:8081","http://localhost:8081","http://localhost:63342", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
