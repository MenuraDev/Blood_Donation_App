package com.blooddonation.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
        registry.addViewController("/blood-donation-events").setViewName("blood-donation-events");
        registry.addViewController("/strategy-management").setViewName("strategy-management");
        registry.addViewController("/donations-history").setViewName("donation-history");
        registry.addViewController("/event-register-management").setViewName("event-register-management");
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/vendor/**")
                .addResourceLocations("classpath:/static/vendor/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}