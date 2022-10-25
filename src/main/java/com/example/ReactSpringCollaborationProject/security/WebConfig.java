package com.example.ReactSpringCollaborationProject.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//이해가 안감
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(),
                    HttpMethod.PUT.name(), HttpMethod.DELETE.name())
                .exposedHeaders("*");
    }

}
