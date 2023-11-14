package com.hacker.hackathon;

import com.hacker.hackathon.config.resolver.ServiceTokenResolver;
import com.hacker.hackathon.config.resolver.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Configuration
public class HackathonApplication implements WebMvcConfigurer {
    private final UserIdResolver userIdResolver;
    private final ServiceTokenResolver serviceTokenResolver;

    public static void main(String[] args) {
        SpringApplication.run(HackathonApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://172.20.10.3:3000/", "http://172.21.116.227:3000/", "http://localhost:3001/")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(serviceTokenResolver);
    }
}
