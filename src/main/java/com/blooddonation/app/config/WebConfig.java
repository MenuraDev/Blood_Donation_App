package com.blooddonation.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("signup");
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/user-management").setViewName("user-management");
        registry.addViewController("/donor-signup").setViewName("donor_signup");
        registry.addViewController("/blood-request-management").setViewName("blood-request-management");
        registry.addViewController("/blood-unit-management").setViewName("blood-unit-management");
        registry.addViewController("/event-management").setViewName("event-management");
        registry.addViewController("/donation-management").setViewName("donation-management");
    }
}
